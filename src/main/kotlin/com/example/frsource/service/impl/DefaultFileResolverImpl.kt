package com.example.frsource.service.impl

import com.example.frsource.model.FrDataSource
import com.example.frsource.model.FrDataSourceColumn
import com.example.frsource.model.FrEntryFile
import com.example.frsource.service.IFileResolver
import org.springframework.stereotype.Service
import java.io.File

/**
 * 文件解析服务默认实现
 */
@Service
class DefaultFileResolverImpl : IFileResolver {

    /**
     * 数据源正则
     */
    private val dataSourceRegex by lazy {
        Regex("(?<=TableData name=\").*?(?=\")", setOf(RegexOption.IGNORE_CASE))
    }

    private val queryTextRegex by lazy {
        Regex("(?<=<QueryText>.{0,100}?\\[)\\{.{0,10000}?}(?=\\].*?</QueryText>)", RegexOption.DOT_MATCHES_ALL)
    }

    /**
     * 类名正则
     */
    private val typeNameRegex by lazy {
        Regex("(?<=\"TypeName\":\").*?(?=\")", setOf(RegexOption.IGNORE_CASE))
    }

    /**
     * 列名正则
     */
    private val columnNameRegex by lazy {
        Regex("dsName=\".*?\" *?columnName=\".*\"", setOf(RegexOption.IGNORE_CASE))
    }

    /**
     * 解析指定的文件
     */
    override fun resolve(path: String, fileName: String): FrEntryFile? {
        val file = File(fileName)
        if (!file.isFile || !file.exists() || file.extension != "cpt")
            return null
        val text = file.readText()
        if(!text.contains("DatabaseName"))
            return null
        val dataSourceTexts = this.dataSourceRegex.findAll(text)
        val queryTexts = this.queryTextRegex.findAll(text)
        val typeNameTexts = this.typeNameRegex.findAll(text)
        val columnNameTexts = this.columnNameRegex.findAll(text).groupBy({ k ->
            Regex("(?<=dsName=\").*?(?=\")").find(k.value)!!.value
        }, { v ->
            Regex("(?<=columnName=\").*?(?=\")").find(v.value)!!.value
        })

        val queryTextTypeNames = queryTexts.zip(typeNameTexts).map { pair ->
            Pair(pair.first.value.trim('\r', '\n'), pair.second.value)
        }

        val frDataSources = dataSourceTexts.zip(queryTextTypeNames).map {
            var columns = columnNameTexts[it.first.value]?.distinct()?.map { p -> FrDataSourceColumn(p) }
            if(columns == null)
                columns = listOf()
            FrDataSource(name =  it.first.value,
                    typeName =  it.second.second,
                    queryText =  it.second.first,
                    columns =  columns )
        }
        val relativePath = file.path.replace('\\', '/').removePrefix(path)
        return FrEntryFile(file.name, relativePath, frDataSources.toList())
    }
}
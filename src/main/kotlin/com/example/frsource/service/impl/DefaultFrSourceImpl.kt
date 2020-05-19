package com.example.frsource.service.impl

import com.example.frsource.model.FrEntryFile
import com.example.frsource.service.IFileResolver
import com.example.frsource.service.IFileWatch
import com.example.frsource.service.IFrSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.Resource

/**
 * 默认的帆软源服务实现
 */
@Service
class DefaultFrSourceImpl : IFrSource {

    /**
     * 帆软根路径
     */
    @Value("\${config.frRootPath}")
    private lateinit var frRootPath: String

    @Value("\${config.frIgnorePaths}")
    private lateinit var frIgnorePaths: Array<String>

    /**
     * 文件解析服务
     */
    @Resource
    private lateinit var fileResolver: IFileResolver

    /**
     * 文件监听服务
     */
    private lateinit var fileWatch: IFileWatch

    /**
     * 开始监听帆软文件更改
     */
    @PostConstruct
    override fun watch() {
        this.fileWatch = DefaultFileWatchImpl(this.frRootPath, this.frIgnorePaths, true, this.fileResolver)
        this.fileWatch.start()
    }

    /**
     * 根据指定的关键字查询帆软文件
     */
    override fun query(subDir: String, keywords: String): List<FrEntryFile> {
        return this.fileWatch.fileMap.filterKeys { p -> p.startsWith("/$subDir") && p.contains(keywords, true) }
                .map { p -> p.value }
                .sortedBy { p -> p.fileName }
    }

    /**
     * 根据指定的文件名, 获取帆软文件
     */
    override fun findByName(fileName: String): FrEntryFile? {
        return this.fileWatch.fileMap.filterKeys { p -> p.endsWith(fileName, true) }.values.first()
    }
}
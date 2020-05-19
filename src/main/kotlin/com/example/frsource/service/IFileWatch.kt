package com.example.frsource.service

import com.example.frsource.model.FrEntryFile

/**
 * 定义文件监听接口
 */
interface IFileWatch {

    /**
     * 指定的监听目录
     */
    val rootPath: String

    /**
     * 指定忽略的目录
     */
    val ignorePaths : Array<String>

    /**
     * 指定是否包含子目录
     */
    val includeSubDirectories : Boolean

    /**
     * 文件信息容器
     */
    val fileMap : Map<String, FrEntryFile>

    /**
     * 文件解析器
     */
    val fileResolver : IFileResolver

    /**
     * 启动监听
     */
    fun start()

    /**
     * 首次加载
     */
    fun onLoad()

    /**
     * 创建
     */
    fun onCreate(path: String)

    /**
     * 删除
     */
    fun onDelete(path: String)

    /**
     * 修改
     */
    fun onModify(path: String)
}
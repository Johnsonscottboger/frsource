package com.example.frsource.service

import com.example.frsource.model.FrEntryFile

/**
 * 定义帆软源接口
 */
interface IFrSource {

    /**
     * 开始监听帆软文件更改
     */
    fun watch()

    /**
     * 根据指定的关键字查询帆软文件
     */
    fun query(subDir: String, keywords: String) : List<FrEntryFile>

    /**
     * 根据指定的文件名, 获取帆软文件
     */
    fun findByName(fileName: String) : FrEntryFile?
}
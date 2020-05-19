package com.example.frsource.service

import com.example.frsource.model.FrEntryFile

/**
 * 定义文件解析方法
 */
interface IFileResolver {

    /**
     * 解析指定的文件
     */
    fun resolve(path: String, fileName: String) : FrEntryFile?
}
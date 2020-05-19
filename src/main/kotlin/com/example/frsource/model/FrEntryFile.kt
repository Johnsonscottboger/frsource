package com.example.frsource.model

/**
 * 帆软文件信息
 */
data class FrEntryFile(
        /**
         * 文件名
         */
        val fileName: String,

        /**
         * 相对路径, 包含文件名
         */
        val relativePath: String,

        /**
         * 数据源
         */
        val dataSources : List<FrDataSource> = listOf()) {
}
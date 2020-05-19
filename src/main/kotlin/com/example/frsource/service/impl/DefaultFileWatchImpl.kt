package com.example.frsource.service.impl

import com.example.frsource.model.FrEntryFile
import com.example.frsource.service.IFileResolver
import com.example.frsource.service.IFileWatch
import com.sun.nio.file.ExtendedWatchEventModifier
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.ConcurrentHashMap

/**
 * 文件监听服务默认实现
 */
class DefaultFileWatchImpl(override val rootPath: String,
                           override val ignorePaths: Array<String>,
                           override val includeSubDirectories: Boolean = false,
                           override val fileResolver: IFileResolver) : IFileWatch {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val map = ConcurrentHashMap<String, FrEntryFile>()

    init {
        onLoad()
    }
    /**
     * 文件信息容器
     */
    override val fileMap: Map<String, FrEntryFile>
        get() = this.map

    /**
     * 启动监听
     */
    override fun start() {
        val watchService = FileSystems.getDefault().newWatchService()
        val path = File(this.rootPath).toPath()
        if (this.includeSubDirectories) {
            path.register(watchService, arrayOf(StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY), ExtendedWatchEventModifier.FILE_TREE)
        } else {
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)
        }
        GlobalScope.launch {
            watchService.use {
                while (true) {
                    val key = watchService.take()
                    for (event in key.pollEvents()) {
                        val changed = event.context() as Path
                        val p = this@DefaultFileWatchImpl.rootPath + "/" + changed.toString().replace('\\', '/')
                        if(this@DefaultFileWatchImpl.ignorePaths.any { c -> p.startsWith(c)})
                            continue
                        when (event.kind()) {
                            StandardWatchEventKinds.ENTRY_CREATE -> {
                                onCreate(p)
                            }
                            StandardWatchEventKinds.ENTRY_DELETE -> {
                                onDelete(p)
                            }
                            StandardWatchEventKinds.ENTRY_MODIFY -> {
                                onModify(p)
                            }
                        }
                    }
                    key.reset()
                }
            }
        }
    }

    /**
     * 首次加载
     */
    override fun onLoad() {
        val path = File(this.rootPath).toPath()
        Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                log.info("遍历目录/文件:${file.toString()}")
                if(this@DefaultFileWatchImpl.ignorePaths.any { p -> file.toString().replace('\\', '/').startsWith(p)}) {
                    log.info("忽略目录/文件:${file.toString()}")
                    return FileVisitResult.SKIP_SUBTREE
                }
                GlobalScope.launch {
                    onCreate(file.toString().replace('\\', '/'))
                }
                return FileVisitResult.CONTINUE
            }
        })
    }

    /**
     * 创建
     */
    override fun onCreate(path: String) {
        val file = File(path)
        if (!file.isFile || !file.exists() || file.extension != "cpt")
            return
        val entry = this.fileResolver.resolve(this.rootPath, path)
        if(entry != null) {
            val key = path.removePrefix(this.rootPath)
            this.map[key] = entry
        }
    }

    /**
     * 删除
     */
    override fun onDelete(path: String) {
        val key = path.removePrefix(this.rootPath)
        this.map.remove(key)
    }

    /**
     * 修改
     */
    override fun onModify(path: String) {
        onCreate(path)
    }
}
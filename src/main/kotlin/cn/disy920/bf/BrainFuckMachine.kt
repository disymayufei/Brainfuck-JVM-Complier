package cn.disy920.cn.disy920.bf

import java.util.*

class BrainFuckMachine {
    private var memory: ShortArray = ShortArray(15)  // 初始化内存大小为15字节，均以0填充，使用Short是为了防止溢出报错
    private var pointer: Int = 7
    private val scanner: Scanner = Scanner(System.`in`)

    companion object {
        val genCodePart = null  // 标志位，用于表示需要生成BrainFuck编译后代码的地方
    }

    fun moveLeft(step: Int) {
        if (step < 0) {
            moveRight(-step)
            return
        }

        if (pointer - step < 0) {
            // 拷贝原内存，为左方开辟新空间
            var newSize = memory.size * 2
            while (newSize / 2 < (step - pointer)) {
                newSize *= 2
            }

            val newMemory = ShortArray(newSize)
            System.arraycopy(memory, 0, newMemory,(newSize - memory.size - 1), memory.size)

            memory = newMemory


            // 修改指针位置，保证符合预期
            pointer = newSize - memory.size - 1 + pointer - step
        }

        else {
            pointer -= step
        }
    }

    fun moveRight(step: Int) {
        if (step < 0) {
            moveLeft(-step)
            return
        }

        if (pointer + step >= memory.size) {
            // 拷贝原内存，为右方开辟新空间
            var newSize = memory.size * 2
            while (newSize / 2 < (step - (memory.size - pointer - 1))) {
                newSize *= 2
            }

            val newMemory = memory.copyOf(newSize)

            memory = newMemory
        }

        pointer += step
    }

    fun byteAdd(num: Int) {
        val byte: Long = memory[pointer].toLong()
        memory[pointer] = ((byte + num) % 256).toShort()
    }

    fun printByte() {
        print(memory[pointer].toInt().toChar())
    }

    fun inputByte() {
        memory[pointer] = scanner.next()[0].code.toShort()
    }

    fun getByteVal(): Short {
        return memory[pointer]
    }

}
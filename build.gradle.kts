import java.nio.charset.StandardCharsets

val brainfuckFileName: String by project

plugins {
    kotlin("jvm") version "1.9.21"
}

group = "cn.disy920"
version = "1.0.0"

class CompilationException(message: String) : RuntimeException("An error occurred while compiling the code: $message")

fun compileBFCode(code: String): String {
    val builder: StringBuilder = StringBuilder("val machine = BrainFuckMachine()\n")
    var reg = 0
    var index = 0

    var checkerFlag = 0  // 用于检查左右方括号是否匹配

    while (index < code.length) {
        when (code[index]) {
            '>' -> {
                reg++

                if (index < code.length - 1) {
                    index++

                    while (index < code.length) {
                        when (code[index]) {
                            '>' -> reg++
                            '<' -> reg--
                            else -> {
                                builder.append("machine.moveRight($reg)\n")
                                reg = 0
                                index--
                                break
                            }
                        }

                        index++
                    }
                }
            }

            '<' -> {
                reg++

                if (index < code.length - 1) {
                    index++

                    while (index < code.length) {
                        when (code[index]) {
                            '>' -> reg--
                            '<' -> reg++
                            else -> {
                                builder.append("machine.moveLeft($reg)\n")
                                reg = 0
                                index--
                                break
                            }
                        }

                        index++
                    }
                }
            }

            '+' -> {
                reg++

                if (index < code.length - 1) {
                    index++

                    while (index < code.length) {
                        when (code[index]) {
                            '+' -> reg++
                            '-' -> reg--
                            else -> {
                                builder.append("machine.byteAdd($reg)\n")
                                reg = 0
                                index--
                                break
                            }
                        }

                        index++
                    }
                }
            }

            '-' -> {
                reg--

                if (index < code.length - 1) {
                    index++

                    while (index < code.length) {
                        when (code[index]) {
                            '+' -> reg++
                            '-' -> reg--
                            else -> {
                                builder.append("machine.byteAdd($reg)\n")
                                reg = 0
                                index--
                                break
                            }
                        }

                        index++
                    }
                }
            }

            '.' -> builder.append("machine.printByte()\n")
            ',' -> builder.append("machine.inputByte()\n")

            '[' -> {
                builder.append("while(machine.getByteVal().toInt() != 0) { \n")
                checkerFlag++
            }
            ']' -> {
                builder.append("if(machine.getByteVal().toInt() == 0) break }\n")
                if (checkerFlag-- < 0) {
                    throw CompilationException("Unpaired square brackets")
                }
            }

            else -> throw CompilationException("Illegal character: ${code[index]}")
        }

        index++
    }

    if (checkerFlag != 0) {
        throw CompilationException("Unpaired square brackets")
    }

    return builder.toString()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.register("compileBrainFuck") {
    doLast {
        val javaCodeDir = file("$projectDir/src/main")
        val javaCodeFile = file("$javaCodeDir/kotlin/cn/disy920/bf/Main.kt")
        val brainFuckFile = file("$javaCodeDir/resources/$brainfuckFileName")

        println("Compiling BrainFuck code...")

        val bfCode = brainFuckFile.readText(StandardCharsets.UTF_8)

        project.copy {
            from(javaCodeFile.absolutePath)
            into("$projectDir/build/temp")
        }

        val javaCode = javaCodeFile.readText(StandardCharsets.UTF_8)

        javaCodeFile.writeText(
            javaCode.replace("BrainFuckMachine.genCodePart", compileBFCode(bfCode))
        )
    }
}

tasks.register("restoreCodeFile") {
    doLast {
        println("Restoring code file...")

        project.copy {
            from("$projectDir/build/temp/Main.kt")
            into("$projectDir/src/main/kotlin/cn/disy920/bf")
        }

        val tempDir = file("$projectDir/build/temp")
        if (tempDir.isDirectory) {
            tempDir.deleteRecursively()
        }
    }
}

tasks.compileKotlin {
    dependsOn("compileBrainFuck")
    finalizedBy("restoreCodeFile")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
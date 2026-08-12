package ch.yanick.bgr.es.mcp

import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies

fun Application.entscheidSucheMcpClientModule() {
    dependencies {
        provide(EsMcpClient::class)
    }
}
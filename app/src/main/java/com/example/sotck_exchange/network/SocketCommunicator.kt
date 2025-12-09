package com.example.stock_exchange.network

import android.util.Log
import com.example.sotck_exchange.domain.dto.StockDTO
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class SocketCommunicator  {

    private val TAG = "SocketCommunicator"

    private var webSocket: WebSocket? = null

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor { message -> Log.d(TAG, message) }
        logging.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val gson = Gson()

    fun connectAndListen(host: String): Flow<Resource<StockDTO>> = callbackFlow {
        val request = Request.Builder().url(host).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                trySend(Resource.Loading)
                Log.d(TAG, "WebSocket Opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Received: $text")
                try {
                    val responseObj = gson.fromJson(text, StockDTO::class.java)
                    trySend(Resource.Success(responseObj))
                } catch (e: Exception) {
                    trySend(Resource.Error(e))
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                if (t is java.net.SocketException) {
                    // Normal shutdown: ignore
                    Log.d(TAG, "WebSocket closed normally: ${t.message}")
                } else {
                    Log.e(TAG, "WebSocket Failure", t)
                }
                trySend(Resource.Error(t))
                close() // safe close
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket Closed: $reason")
                close()
            }
        })

        awaitClose {
            try {
                webSocket?.close(1000, "Flow Collector Closed")
            } catch (e: Exception) {
                // Ignore socket exceptions on close
                Log.d(TAG, "WebSocket already closed or failed to close: ${e.message}")
            }
        }
    }



    fun send(stock: StockDTO) {
        val jsonStock = gson.toJson(stock)
        webSocket?.send(jsonStock)
        Log.d(TAG, "Sent: $jsonStock")
    }
}

package com.example.music_player_of_874wokiite.utils

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NdefMessage
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.NavController

class NfcHandler(
    private val navController: NavController?,
    private val context: Context
) {
    fun handleNfcIntent(intent: Intent) {
        if (navController == null) {
            Log.e("NFC_DEBUG", "NavController is not initialized yet, retrying...")
            Handler(Looper.getMainLooper()).postDelayed({
                handleNfcIntent(intent)
            }, 100) // 100ms 後に再試行
            return
        }

        Log.d("NFC_DEBUG", "Intent received with action: ${intent.action}")

        // NFCがサポートされていない場合の早期リターン
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        if (nfcAdapter == null) {
            Log.e("NFC_DEBUG", "NFC is not supported on this device")
            return
        }

        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMsgs != null && rawMsgs.isNotEmpty()) {
                try {
                    val ndefMessage = rawMsgs[0] as NdefMessage
                    val ndefRecord = ndefMessage.records[0]
                    val rawPayload = ndefRecord.payload

                    // ペイロードの先頭 (メタデータ部分) をスキップ
                    val payload = String(rawPayload.copyOfRange(3, rawPayload.size)) // 3バイトをスキップ

                    Log.d("NFC_DEBUG", "Raw Payload: ${String(rawPayload)}")
                    Log.d("NFC_DEBUG", "Processed Payload: $payload")

                    val pathSegments = payload.split("/")
                    if (pathSegments.size == 3 && pathSegments[0] == "detail") {
                        val musicTitle = pathSegments[1]
                        val albumTitle = pathSegments[2]

                        Log.d("NFC_DEBUG", "Navigating to detail screen: $musicTitle, $albumTitle")

                        navController.navigate("detail/$musicTitle/$albumTitle")
                    } else {
                        Log.e("NFC_DEBUG", "Invalid payload format: $payload")
                    }
                } catch (e: Exception) {
                    Log.e("NFC_DEBUG", "Error reading NDEF message", e)
                }
            } else {
                Log.e("NFC_DEBUG", "No NDEF messages found")
            }
        } else {
            Log.e("NFC_DEBUG", "Unexpected intent action: ${intent.action}")
        }
    }
}

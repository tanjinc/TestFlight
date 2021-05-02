package com.jacktan.utils

import com.jacktan.ApiManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.*


object DownloadUtil {

    fun downloadFile(jobName: String, buildNumber: Int, relativePath: String, target:String):  Observable<Float> {
        return ApiManager.getApiService()
                .getApkFile(jobName, buildNumber, relativePath)
                .flatMap{
                    DownResponseObservable(it, target)
                }
                .observeOn(Schedulers.computation())
                .compose(RxUtils.applySchedulers())
    }
    class DownResponseObservable(private var body: ResponseBody?, private var targetUrl: String) : Observable<Float>() {

        override fun subscribeActual(observer: Observer<in Float>?) {
            writeResponseBodyToDisk(body, targetUrl, observer)
        }

        private fun writeResponseBodyToDisk(body: ResponseBody?, targetUrl: String, observer: Observer<in Float>?) : Boolean{
            if (body == null) {
                return false
            }

            var inputStream: InputStream?= null
            var outputStream: OutputStream?= null

            try {
                val futureStudioIconFile = File(targetUrl)
                val fileReader = ByteArray(8 * 1024 * 1024)
                val fileSize: Long = body.contentLength()
                var fileSizeDownloaded = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    var read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read
                    observer?.onNext(fileSizeDownloaded * 1.0f /fileSize)
                }
                outputStream.flush()
                observer?.onComplete()
                return true
            } catch (e : IOException) {
                observer?.onError(e)
                return false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        }
    }
}
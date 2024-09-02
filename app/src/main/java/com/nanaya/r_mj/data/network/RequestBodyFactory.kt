import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

fun getRequestBody(params: Map<String, Any>): RequestBody {
    return JSONObject(params).toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

fun getRequestBody(jsonObject: JSONObject): RequestBody {
    return jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

fun getRequestBody(json: String): RequestBody {
    return json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

/**
 * 通过对象获取请求体
 */
fun getRequestBody(`object`: Any): RequestBody {
    val json = Gson().toJson(`object`)
    return json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

fun pathsToMultipartBodyParts(pasth: List<String>): List<MultipartBody.Part>? {
    val files = ArrayList<File>()
    for (s in pasth) {
        val f = File(s)
        if (f.exists()) {
            files.add(f)
        }
    }
    return filesToMultipartBodyParts(files)
}

fun filesToMultipartBodyParts(files: List<File>?): List<MultipartBody.Part>? {
    if (files == null) return null
    val parts = ArrayList<MultipartBody.Part>(files.size)
    for (file in files) {
        val requestBody = file.asRequestBody(MultipartBody.FORM)
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        parts.add(part)

    }
    return parts
}
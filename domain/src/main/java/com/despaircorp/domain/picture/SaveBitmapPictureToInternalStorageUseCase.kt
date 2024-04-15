package com.despaircorp.domain.picture

import android.net.Uri
import javax.inject.Inject

class SaveBitmapPictureToInternalStorageUseCase @Inject constructor(
    private val pictureDomainRepository: PictureDomainRepository,
    private val transformUriToBitmapUseCase: TransformUriToBitmapUseCase,
) {
    suspend fun invoke(uri: Uri, name: String, id: Int): String {
        val bitmap = transformUriToBitmapUseCase.invoke(uri)
        val fileName = "${id}_${name}_${System.currentTimeMillis()}.bmp"
        return pictureDomainRepository.saveImageToInternalStorage(bitmap ?: return "", fileName)
    }
    
}
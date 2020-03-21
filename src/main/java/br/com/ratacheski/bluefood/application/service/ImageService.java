package br.com.ratacheski.bluefood.application.service;

import br.com.ratacheski.bluefood.domain.image.ImageTypeEnum;
import br.com.ratacheski.bluefood.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;


@Service
public class ImageService {

    @Value("${bluefood.files.logotipo}")
    private String logotiposDir;

    @Value("${bluefood.files.categoria}")
    private String categoriasDir;

    @Value("${bluefood.files.comida}")
    private String comidasDir;

    public void uploadLogotipo(MultipartFile file, String fileName) {
        try {
            IOUtils.copy(file.getInputStream(), fileName, logotiposDir);
        } catch (IOException e) {
            throw new ApplicationServiceException(e);
        }
    }

    public void uploadComida(MultipartFile file, String fileName) {
        try {
            IOUtils.copy(file.getInputStream(), fileName, comidasDir);
        } catch (IOException e) {
            throw new ApplicationServiceException(e);
        }
    }

    public byte[] getBytes(String type, String imgName) {
        byte[] bytes;
        try {
            if (type.equals(ImageTypeEnum.COMIDA.getId())) {
                bytes = IOUtils.getBytes(Paths.get(comidasDir, imgName));
            } else if (type.equals(ImageTypeEnum.CATEGORIA.getId())) {
                bytes = IOUtils.getBytes(Paths.get(categoriasDir, imgName));
            } else if (type.equals(ImageTypeEnum.LOGOTIPO.getId())) {
                bytes = IOUtils.getBytes(Paths.get(logotiposDir, imgName));
            } else {
                throw new Exception(type + "Não é um tipo de imagem válida");
            }
        } catch (Exception e) {
            throw new ApplicationServiceException(e);
        }
        return bytes;
    }
}

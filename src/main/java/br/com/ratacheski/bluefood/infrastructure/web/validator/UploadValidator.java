package br.com.ratacheski.bluefood.infrastructure.web.validator;

import br.com.ratacheski.bluefood.util.FileType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class UploadValidator implements ConstraintValidator<UploadConstraint, MultipartFile> {

    private List<FileType> acceptedFileTypes;
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null){
            return true;
        }
        for(FileType fileType : acceptedFileTypes){
            if (fileType.sameOf(value.getContentType())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void initialize(UploadConstraint constraintAnnotation) {
        acceptedFileTypes = Arrays.asList(constraintAnnotation.acceptedTypes());
    }
}

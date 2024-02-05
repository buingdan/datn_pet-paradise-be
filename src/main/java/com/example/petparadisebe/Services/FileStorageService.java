package com.example.petparadisebe.Services;


import com.example.petparadisebe.config.FileStorageProperties;
import com.example.petparadisebe.exception.FileNotFoundException;
import com.example.petparadisebe.exception.FileStorageException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileLogoStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileLogoStorageLocation = Paths.get(fileStorageProperties.getUploadLogoDir())
                .toAbsolutePath().normalize();

        try{
            Files.createDirectories(fileLogoStorageLocation);
        }catch (Exception ex){
            throw new FileStorageException("Không thể tạo địa chỉ mà file đăng tải được lưu trữ", ex);
        }
    }

    public  String storeLogoFile(MultipartFile file){
        return storeFile(fileLogoStorageLocation, file);
    }
    private  String storeFile(Path location, MultipartFile file){
        UUID uuid = UUID.randomUUID();

        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = uuid.toString() + "." +ext;

        try{
            if(filename.contains("..")){
                throw new FileStorageException("Tên file chứa các kí tự ko hợp lệ" + filename);
            }

            Path targetLocation = location.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (Exception exception){
            throw new FileStorageException("Không thể lưu file " + filename + " . Vui lòng thử lại" , exception);
        }
    }

    public  Resource loadLogoFileAsResource(String filename){
        return  loadFileasResource(fileLogoStorageLocation, filename);
    }
    private Resource loadFileasResource (Path location, String filename){
        try{
            Path filePath = location.resolve(filename).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()){
                return resource;
            }else {
                throw new FileNotFoundException("File không tồn tại " +filename);
            }
        }catch (Exception ex){
            throw new FileNotFoundException("File không tồn tại" + filename, ex);
        }
    }

    public void deleteLogoFile(String filename){
        deleteFile(fileLogoStorageLocation, filename);
    }
    private void deleteFile(Path location, String filename){
        try{
            Path filePath = location.resolve(filename).normalize();

            if(!Files.exists(filePath)){
                throw new FileNotFoundException("Không tìm thấy file " + filename);
            }
            Files.delete(filePath);
        }catch (Exception ex){
            throw new FileNotFoundException("Không tìm thấy file " + filename, ex);
        }
    }

}

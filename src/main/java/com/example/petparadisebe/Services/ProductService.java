package edu.poly.springshop.service;

import edu.poly.springshop.domain.Category;
import edu.poly.springshop.domain.Manufacturer;
import edu.poly.springshop.dto.ManufacturerDto;
import edu.poly.springshop.exception.CategoryException;
import edu.poly.springshop.exception.ManufacturerException;
import edu.poly.springshop.repository.ManufacturerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private  FileStorageService fileStorageService;

    public Manufacturer insertManuFacturer(ManufacturerDto dto){
        List<?> foundlist = manufacturerRepository.findByNameContainsIgnoreCase(dto.getName());

        if(foundlist.size() > 0){
            throw new ManufacturerException("Tên manufacturer đã tồn tại ");
        }

        Manufacturer entity = new Manufacturer();

        BeanUtils.copyProperties(dto, entity);

        if(dto.getLogoFile() != null){
            String filename = fileStorageService.storeLogoFile(dto.getLogoFile());

            entity.setLogo(filename);
            dto.setLogoFile(null);
        }

        return manufacturerRepository.save(entity);
    }

    public List<Manufacturer> findAll() {
        return manufacturerRepository.findAll();
    }

    public Page<Manufacturer> findAll(Pageable pageable) {
        return manufacturerRepository.findAll(pageable);
    }

    public Page<Manufacturer> findByName(String name, Pageable pageable) {
        return manufacturerRepository.findByNameContainsIgnoreCase(name, pageable);
    }

    public Manufacturer findById(Long id) {
        Optional<Manufacturer> found = manufacturerRepository.findById(id);

        if(found.isEmpty()){
            throw new CategoryException("Manufacturer with id " + id + " does not exist");
        }
        return found.get();
    }

    public void deleteById(Long id) {
        Manufacturer existed = findById(id);
        manufacturerRepository.deleteById(id);
    }

    public Manufacturer updateManuFacturer(Long id, ManufacturerDto dto){
        var found = manufacturerRepository.findById(id);

        if(found.isEmpty()){
            throw new ManufacturerException("Tên manufacturer không tồn tại ");
        }

        Manufacturer entity = new Manufacturer();

        BeanUtils.copyProperties(dto, entity);

        if(dto.getLogoFile() != null){
            String filename = fileStorageService.storeLogoFile(dto.getLogoFile());

            entity.setLogo(filename);
            dto.setLogoFile(null);
        }

        return manufacturerRepository.save(entity);
    }
}

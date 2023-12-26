package com.mlc.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.mlc.entity.ArchiveProduct;
import com.mlc.entity.Response;
import com.mlc.repository.AppModuleRepository;
import com.mlc.repository.ProductArchiveRepository;
import com.mlc.repository.ProductRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.mlc.entity.Product;

@Service
@RequiredArgsConstructor
public class ProductService{
private final AppModuleRepository appModuleRepository;
private final ProductRepository productRepository;
private final ProductArchiveRepository productArchiveRepository;
@Autowired
public ProductService(ProductRepository productRepository,ProductArchiveRepository productArchiveRepository, AppModuleRepository appModuleRepository) {
		this.productRepository = productRepository;
		 this.productArchiveRepository = productArchiveRepository;
		 this.appModuleRepository=appModuleRepository;
	}

	public List<Product> viewAllProducts() {
		return productRepository.findAll();

	}

	public Optional<Product> viewProductById(Long id) {
		return productRepository.findById(id);

	}

	public Response saveProduct(Product product) {
		Response response = new Response();
		try{
			productRepository.save(product);
			response.setStatusCode(201);
			response.setStatusMessage("product created successfully");
		}catch (Exception e){
			response.setStatusCode(409);
			response.setStatusMessage("failed");
		}
		return response;
	}

	public void deleteProduct(Long id) {
		Optional<Product> product = productRepository.findById(id);
		if(product.isPresent())
		{
			ArchiveProduct archive = new ArchiveProduct();
			BeanUtils.copyProperties(product.get(), archive);
			productArchiveRepository.save(archive);
			productRepository.deleteById(id);
		}
		
	}

	public List<Product> searchProducts(String query) {
        return productRepository.searchProducts(query);
    }


	public void generateExcel(HttpServletResponse response) throws IOException {
		List<Product> product = productRepository.findAll();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet =workbook.createSheet("product_list");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Id");
		row.createCell(1).setCellValue("ProductName");
		row.createCell(2).setCellValue("Material");
		row.createCell(3).setCellValue("Weight");
		row.createCell(4).setCellValue("NoOfComponents");

		int datarowindex =1;
	for(Product product1 : product)
	{
	HSSFRow dataRow = sheet.createRow(datarowindex);
	dataRow.createCell(0).setCellValue(product1.getId());
		dataRow.createCell(1).setCellValue(product1.getProductName());
		dataRow.createCell(2).setCellValue(product1.getMaterial());
		dataRow.createCell(3).setCellValue(product1.getNumberOfComponents());
		dataRow.createCell(4).setCellValue(product1.getWeight());
		datarowindex++;
	}
ServletOutputStream ops = response.getOutputStream();
workbook.write(ops);
workbook.close();
ops.close();
	}
}

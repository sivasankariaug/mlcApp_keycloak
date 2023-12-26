package com.mlc.service;

import com.mlc.entity.AppModule;
import com.mlc.entity.ApplicationMenu;
import com.mlc.repository.AppModuleRepository;
import com.mlc.repository.ApplicationMenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;



@Service
@RequiredArgsConstructor
public class AppModuleService {

    private final AppModuleRepository appModuleRepository;
    private final ApplicationMenuRepository applicationMenuRepository;



    @Transactional
    public void importModulesFromExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            processModulesSheet(workbook.getSheetAt(0));
            processMenusSheet(workbook.getSheetAt(1));
        } catch (IOException e) {
            throw new RuntimeException("Error processing Excel file", e);
        }

    }

    private void processMenusSheet(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }

            AppModule module = new AppModule();
            module.setModuleName(getStringValue(row.getCell(1)));
            module.setDescription(getStringValue(row.getCell(2)));

            appModuleRepository.save(module);
        }
    }
    private void processModulesSheet(Sheet sheet) {
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }
            ApplicationMenu menu = new ApplicationMenu();
            menu.setMenuName(getStringValue(row.getCell(1)));
            menu.setDescription(getStringValue(row.getCell(2)));

            Cell moduleIdCell = row.getCell(3);
            Long moduleId = null;
            if (moduleIdCell != null && moduleIdCell.getCellType() == CellType.NUMERIC) {
                moduleId = (long) moduleIdCell.getNumericCellValue();
            }

            final Long finalModuleId = moduleId; // effectively final variable
            AppModule module = finalModuleId != null ? appModuleRepository.findById(finalModuleId)
                    .orElseThrow(() -> new RuntimeException("Module not found with ID: " + finalModuleId)) : null;
            menu.setModule(module);

            Cell parentMenuCell = row.getCell(4);
            Long parentMenuId = null;
            if (parentMenuCell != null && parentMenuCell.getCellType() == CellType.NUMERIC) {
                parentMenuId = (long) parentMenuCell.getNumericCellValue();
            }

            final Long finalParentMenuId = parentMenuId; // effectively final variable
            ApplicationMenu parentMenu = finalParentMenuId != null ? applicationMenuRepository.findById(finalParentMenuId)
                    .orElseThrow(() -> new RuntimeException("Parent Menu not found with ID: " + finalParentMenuId)) : null;
            menu.setParentMenu(parentMenu);

            applicationMenuRepository.save(menu);
        }
    }
    private String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }


}

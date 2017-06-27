package com.signs.util;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class BigExcelUtil {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private String filename;
    private SheetContentsHandler handler;
    private InputStream tempInputStream;

    public BigExcelUtil(String filename) {
        this.filename = filename;
    }

    public BigExcelUtil(InputStream inputStream) {
        this.tempInputStream = inputStream;
    }

    public BigExcelUtil setHandler(SheetContentsHandler handler) {
        this.handler = handler;
        return this;
    }

    public void parse() {
        OPCPackage pkg = null;
        InputStream sheetInputStream = null;
        try {
            if(tempInputStream != null)
                pkg = OPCPackage.open(tempInputStream);
            else
                pkg = OPCPackage.open(filename, PackageAccess.READ);
            XSSFReader xssfReader = new XSSFReader(pkg);
            StylesTable styles = xssfReader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            sheetInputStream = xssfReader.getSheetsData().next();
            processSheet(styles, strings, sheetInputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (sheetInputStream != null) {
                try {
                    sheetInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (pkg != null) {
                try {
                    pkg.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream) throws SAXException, ParserConfigurationException, IOException {
        XMLReader sheetParser = SAXHelper.newXMLReader();
        Assert.notNull(handler, "请调用setHandler方法");
        sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, handler, false));
        sheetParser.parse(new InputSource(sheetInputStream));
    }
}

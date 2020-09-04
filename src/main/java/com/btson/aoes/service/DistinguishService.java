package com.btson.aoes.service;

import com.btson.aoes.domain.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DistinguishService {
    /*
    * 本Service层负责用户上传的Excel，Word的读取和识别
    * */
    @Resource
    private QuestionService questionService;
    @Resource
    private UserService userService;
    @Resource
    private CatalogueService catalogueService;


    //上传文件
    public String uploadFile(List<MultipartFile> uploadfile,String diyPath) throws FileNotFoundException {
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        String filePaths = null;
        // 判断所上传文件是否存在
        if (!uploadfile.isEmpty() && uploadfile.size() > 0) {
            //循环输出上传的文件
            for (MultipartFile file : uploadfile) {
                // 获取上传文件的原始名称
                String originalFilename = file.getOriginalFilename();
                // 设置上传文件的保存地址目录
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                File upload = new File(path.getAbsolutePath(), diyPath);
                String dirPath = upload + "//";
                //String dirPath = "E:/Project/IDEAS/mvcDemo-master/src/main/webapp/upload/";
                File filePath = new File(dirPath);
                // 如果保存文件的地址不存在，就先创建目录
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                // 使用UUID重新命名上传的文件名称(上传人_uuid_原始文件名称)
                String newFilename = user_id + "_" + UUID.randomUUID() + "_" + originalFilename;
                try {
                    // 使用MultipartFile接口的方法完成文件上传到指定位置
                    file.transferTo(new File(dirPath + newFilename));
                    filePaths = newFilename;//返回文件名
                    return filePaths;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error";
                }
            }
            return filePaths;
        }else {
            return "error";
        }
    
    }

//    获取文件
    public Workbook  getFile(String filePath){
        Workbook wb=null;
        File file=new File(filePath);
        if(!file.exists()){
            System.out.println("文件不存在");
            wb=null;
        }
        else {
            String fileType=filePath.substring(filePath.lastIndexOf("."));//获得后缀名
            try {
                InputStream is = new FileInputStream(filePath);
                if(".xls".equals(fileType)){
                    wb = new HSSFWorkbook(is);
                }else if(".xlsx".equals(fileType)){
                    wb = new XSSFWorkbook(is);
                }else{
                    System.out.println("格式不正确");
                    wb=null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return wb;
    }

    //读取文件
    public void readExcelFile(Workbook wb,String fileName){
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        List<Question> questionList=new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);//读取sheet(从0计数)
        int rowNum = sheet.getLastRowNum();//读取行数(从0计数)
        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);//获得行
            Question question=new Question();
            question.setUser_id(String.valueOf(user_id));
            question.setSource(fileName);
            question.setCreate_time(new Date());
            int colNum = row.getLastCellNum();//获得当前行的列数
            List<Options> optionsList=new ArrayList<>();
            for (int j = 0; j < colNum; j++) {
                Cell cell = row.getCell(j);//获取单元格
                if (cell == null||cell.getCellTypeEnum()==CellType.BLANK){
                } else{
                    if(j==0){ //分类
                            question.setQuestion_class("99");
                    }else if(j==1){ //题型
                        question.setQuestion_type(cell.toString());

                    }else if(j==2){//难度
                        question.setQuestion_level(cell.toString());

                    }else if (j==3){//解析
                        question.setQuestion_analysis(cell.toString());

                    }else if (j==4){  //题干
                        question.setQuestion_title(cell.toString());
                    } else{
                        Options option=new Options();
                            option.setOpt_content(cell.toString());
                            option.setQuestion(question);
                            short fontValue = cell.getCellStyle().getFontIndex(); //获取字体
                            if(fontValue==5){
                                option.setOpt_status(1);
                            }else{
                                option.setOpt_status(0);
                            }
                            optionsList.add(option);
                            question.setOptions(optionsList);
                    }
                }

            }
            System.out.println();
            questionList.add(question);
        }
        questionService.saveAllQuestions(questionList);
    }

}
package com.btson.aoes.controller;

import com.btson.aoes.domain.BaseDict;
import com.btson.aoes.domain.Catalogue;
import com.btson.aoes.domain.Question;
import com.btson.aoes.domain.User;
import com.btson.aoes.service.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/question")
public class QuestionController {
    @Resource
    private QuestionService questionService;
    //数据字典
    @Resource
    private BaseDictService baseDictService;
    @Resource
    private CatalogueService catalogueService;
    @Resource
    private DistinguishService distinguishService;
    @Resource
    private UserService userService;
    //题型
    private String QType="001";
    //难度
    private String QLevel="003";


    @RequestMapping("/manage")
    public String questionManage(){
        return "question/question-manage";
    }

    @RequestMapping("/show")
    public String questionShow(){
        return "ok";
    }

    @RequestMapping("/addChoice")
    public String questionCreateChoice(){
        return "question/question-create-choice";
    }

    @RequestMapping("/add.action")
    @ResponseBody
    public Map<String,Object> questionAdd(Question question,String[] opt_content,int[] opt_status,int[] opt_id, Model model, HttpServletRequest request){

        Map<String,Object> map=new HashMap<>();
        System.out.println(question.getQuestion_title());
        //利用session获取当前用户账号密码
        /*HttpSession session=request.getSession();
        User user= (User) session.getAttribute("USER_SESSION");
        String userId1=String.valueOf(user.getId());*/

       Question questionCheck=questionService.questionAdd(question,opt_content,opt_status,opt_id);
        System.out.println(questionCheck.getId());

        if(questionCheck!=null){
            map.put("msg","OK");
        }else{
            map.put("msg","error");
        }
        return map;
    }

    @RequestMapping("/addManual")
    public String questionAddManual(){

        return "question/question-add-manual";
    }

    //数据接口:/question/findAll
    @RequestMapping("/findAll")
    @ResponseBody
    public Map<String,Object> findQuestionAll(Integer page,Integer limit,Question question){
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        question.setUser_id(String.valueOf(user_id));


            if(question.getQuestion_title()==null||question.getQuestion_title().equals("")){
                question.setQuestion_title(null);
            }

            if(question.getQuestion_level()==null||question.getQuestion_level().equals("")){
                question.setQuestion_level(null);
            }

            if(question.getQuestion_class()==null||question.getQuestion_class().equals("")){
                question.setQuestion_class(null);
            }

            if (question.getQuestion_type()==null||question.getQuestion_type().equals("")){
                question.setQuestion_type(null);
            }
        Map<String,Object> map=questionService.findQuestionByCondition(question,page-1,limit);
        return map;
    }
    //数据接口:/question/findAll
    @RequestMapping("/findAllUpload")
    @ResponseBody
    public Map<String,Object> findQuestionTest(Integer page,Integer limit,Question question){
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        question.setUser_id(String.valueOf(user_id));
        if(question.getSource()==null||question.getSource().equals("")){
            question.setSource("123");
        }
        Map<String,Object> map=questionService.findQuestionByCondition(question,page-1,limit);
        return map;
    }

    //数据接口:/question/findOne
    @RequestMapping("/findOne")
    @ResponseBody
    public Map<String,Object> findQuestionById(int id,Model model){
        Map<String,Object> map=new HashMap<>();
        Question question=questionService.findQuestionOne(id);
        String cid=question.getQuestion_class();
        if(question.getQuestion_class()==null||question.getQuestion_class().equals("")){
            cid="99";
        }
        Catalogue catalogue=catalogueService.findOne(Integer.parseInt(cid));
        question.setQuestion_class(catalogue.getTitle());
        model.addAttribute("question",question);
        map.put("question",question);
        map.put("msg","OK");
        return map;
    }

    //数据接口:/question/findOne
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> deleteQuestion(int[] ids){
        Map<String,Object> map=new HashMap<>();
        for(int id:ids){
            System.out.println(id);
            questionService.deleteQuestion(id);
        }
        map.put("msg","OK");
        return map;
    }

    //修改试题
    @RequestMapping("/edit")
    public String editQuestion(int id,Model model){
        Question question=questionService.findQuestionOne(id);
        //类别属性
        List<Catalogue> catalogues=catalogueService.findAllCatalogue(10001);
        List<BaseDict> questionType=baseDictService.findByBaseDict(QType);
        List<BaseDict> questionLevel=baseDictService.findByBaseDict(QLevel);
        model.addAttribute("questionType",questionType);
        model.addAttribute("questionLevel",questionLevel);
        model.addAttribute("catalogues",catalogues);

        model.addAttribute("question",question);
        return "question/question-edit";
    }


    @RequestMapping("/uploadExcel")
    @ResponseBody
    public Map<String,Object> questionAutoExcel(List<MultipartFile> uploadfile) throws FileNotFoundException {
        //当前登录用户id
        int user_id=Integer.parseInt(userService.findUserIdByUsername());
        Map<String,Object> map=new HashMap<>();
        String diyPath="static/uploadfile/user/Excel";//设置上传文件相对路径

        String uploadFileName=distinguishService.uploadFile(uploadfile,diyPath); //上传文件
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        File upload = new File(path.getAbsolutePath(), diyPath);
        String dirPath = upload + "//";
        String AllPath=dirPath+uploadFileName;
        Workbook wb=distinguishService.getFile(AllPath);
        distinguishService.readExcelFile(wb,uploadFileName);

        map.put("msg","OK");
        map.put("source",uploadFileName);
        return map;
    }
    @RequestMapping("/questionAddAuto")
    public String questionAddAuto(){
        return "question/question-add-auto";
    }

    @RequestMapping("/downloadExcel")
    @ResponseBody
    public String downloadExcel(HttpServletResponse response)throws FileNotFoundException{
        // 设置上传文件的保存地址目录
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        String downloadFilePath = "static/uploadfile/templateFile/";//被下载的文件在服务器中的路径,
        String fileName = "question-model.xlsx";//被下载文件的名称
        File file = new File(path.getAbsolutePath(),downloadFilePath+fileName);
        if (file.exists()) {
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);
        OutputStream outputStream = response.getOutputStream();
        int i = bis.read(buffer);
        while (i != -1) {
          outputStream.write(buffer, 0, i);
          i = bis.read(buffer);
        }

            return "下载成功";
          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            if (bis != null) {
                try {
                bis.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            if (fis != null) {
                try {
                fis.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        }
      return "下载失败";
    }

    @RequestMapping("/test")
    public String questionTest(){
        return "login2";
    }
}

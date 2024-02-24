package com.example.quizproject.controller;


import com.example.quizproject.model.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.*;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.ui.Model;
import com.example.quizproject.service.authservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class homecontroller {
    @Autowired
    private authservice service;
    @RequestMapping("/home")
    public String show(){
        return "home";
    }
    @RequestMapping("/register")
    public String register(){
        return "registertration";
    }
    @RequestMapping("/register/student")

    public String add(User user,Model model){
        user.setPassword("{noop}"+user.getPassword());
        System.out.println(user);
        ResponseEntity<String> s=service.addStudent(user);
        model.addAttribute("text",s.getBody());
        return s.getStatusCode()== HttpStatusCode.valueOf(201)?"redirect:/login_page":"errot";

    }
    @RequestMapping("/login_page")
    public String login_page(){
        return "login";
    }
    @RequestMapping("/dashboard")
    public String dashboard( HttpServletRequest request, HttpServletResponse response, FilterChain chain, Model model, Principal principal,@RequestParam(required=false) Integer offset,@RequestParam(required=false) String field,@RequestParam(required=false) Integer dir,@RequestParam(required =false) String search) throws ServletException, IOException {
        model.addAttribute("user",principal.getName());
        if(request.isUserInRole("ROLE_student")){
//            service.get_user_score(principal.getName());
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

        model.addAttribute("entities",service.all_subjects());
        return "student_dashboard";}
        if(request.isUserInRole("ROLE_admin")){
            int size=service.user_size(search);
            model.addAttribute("users",service.all_users(offset,field,search));
            if(search!=null){
                model.addAttribute("search",search);
            }
            else{
                model.addAttribute("search","");
            }
            if(offset==null) {
                model.addAttribute("currentPage", 1);
            }
            else{
//            System.out.println(offset);
                model.addAttribute("currentPage", offset);
            }
            model.addAttribute("totalPages",Math.ceil(size/(double)10));
            model.addAttribute("pageSize",10);
            model.addAttribute("service",service);
            if(field==null){
                model.addAttribute("field","username");
            }
            else{
                model.addAttribute("field",field);
            }
            return "admin_dashboard";
        }
        if(request.isUserInRole("ROLE_teacher")){
            String username=principal.getName();
            model.addAttribute("entities",service.all_quiz(offset,field,dir,search));

            int size=service.sub_size(search);
//            System.out.println(size);
            if(search!=null){
                model.addAttribute("search",search);
            }
            else{
                model.addAttribute("search","");
            }
//            System.out.println(offset+" "+size+" ");
            if(dir==null){
                dir=1;
            }
            if(offset==null) {
                model.addAttribute("currentPage", 1);
            }
            else{
//            System.out.println(offset);
                model.addAttribute("currentPage", offset);
            }
            model.addAttribute("totalPages",(int)Math.ceil(size/(double)10));
            model.addAttribute("pageSize",10);
            if(field==null){
                model.addAttribute("field","user_username");
            }
            else{
                model.addAttribute("field",field);
            }
            System.out.println(offset+" "+Math.ceil(size/(double)10));
            model.addAttribute("dir", dir);
            return "teacher_dashboard";
        }
        return "errot";
    }

    @GetMapping("admin/delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public String delete(@PathVariable String id, Model model, Principal principal, RedirectAttributes redirectAttributes){
        System.out.println(id);
        System.out.println(service.isAdmin(Integer.valueOf(id)));
        System.out.println(service.get_admin_count(principal.getName())==0);
        if(service.isAdmin(Integer.valueOf(id))&&service.get_admin_count(principal.getName())==0){
            redirectAttributes.addFlashAttribute("admin_del","There should be atleast one admin");
            return "redirect:/dashboard";
        }
        service.del_by_id( Integer.valueOf(id));
//        model.addAttribute("users",service.all_users());
        return "redirect:/dashboard";
    }
    @GetMapping("admin/update/{id}")
    @PreAuthorize("hasRole('admin')")
    public String update(@PathVariable String id,Model model,Principal principal){
        model.addAttribute("user1",principal.getName());
        model.addAttribute("service",service);
        User user= service.userbyid(Integer.valueOf(id));
        authorities auth= service.authbyuserid(user);
        Map<String,String> mp=new HashMap<>();
        mp.put("firstname",user.getFirstname());
        mp.put("lastname",user.getLastname());
        mp.put("username", user.getUsername());
        mp.put("password", user.getPassword().substring(6,user.getPassword().length()));
        mp.put("authority", auth.getAuthority().substring(5));
        model.addAttribute("user",mp);
        return "update";
    }
    @RequestMapping(value="admin/update",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PreAuthorize("hasRole('admin')")
    public String updation(@RequestBody MultiValueMap<String, String> mp,Model model){
        System.out.println(mp);
        User user= service.userByUsername((String)mp.get("username").get(0));
        user.setFirstname((String) mp.get("firstname").get(0));
        user.setLastname((String)mp.get("lastname").get(0));
        user.setPassword("{noop}"+(String)mp.get("password").get(0));
        authorities auth = service.authbyuserid(user);
        auth.setAuthority((String)mp.get("role").get(0));
        service.saveandupdate(user,auth);
//        model.addAttribute("users",service.all_users());
        return "redirect:/dashboard";
    }
    @RequestMapping("/admin/register_user")
    public String register_teacher_html(){
        return "register_teacher";
    }
    @RequestMapping(value="/admin/registration_user",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String register_teacher(@RequestBody MultiValueMap<String,String> mp,Model model,Principal principal){
        model.addAttribute("user",principal.getName());

        User user=new User(mp.get("username").get(0),mp.get("firstname").get(0),mp.get("lastname").get(0),mp.get("password").get(0));
        user.setPassword("{noop}"+user.getPassword());
        ResponseEntity<String> st=service.addUser(user,mp.get("role").get(0));
//        model.addAttribute("users",service.all_users());

        return st.getStatusCode()== HttpStatusCode.valueOf(201)?"redirect:/dashboard":"errot1";
    }
    @RequestMapping("/teacher/addquestion")
    public String addque(Principal principal,Model model){
        model.addAttribute("user",principal.getName());
       
        return "Question_form";
    }
    @PostMapping(value="/teacher/addque", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addquestion(@RequestBody MultiValueMap<String,String> mp){
        subject sub=service.getsubjectscore((String)mp.get("subject_name").get(0),(Integer.valueOf(mp.get("score").get(0))));
//        subject sub=new subject((String)mp.get("subject_name").get(0));
        //        System.out.println(que.getSubject_name());
        subject sub1=service.save_subject(sub);
        System.out.println(mp.get("option"+mp.get("Correctoption").get(0)));
        questions que=new questions((String)mp.get("question").get(0),(String)mp.get("option1").get(0),(String)mp.get("option2").get(0),(String)mp.get("option3").get(0),(String)mp.get("option4").get(0),(String)mp.get("option"+mp.get("Correctoption").get(0)).get(0),(Integer.valueOf(mp.get("score").get(0))),sub1);
         System.out.println(que.getSubject_name());
        service.savequestion(que);
        return "redirect:/dashboard";
    }
    @RequestMapping("/teacher/allquestions")
    public  String disp_questions(Model model,@RequestParam(required = false) Integer offset,Principal principal,@RequestParam(required = false) String field,@RequestParam(required = false) String search){
        model.addAttribute("user", principal.getName());
        int size=service.get_questions_size(search);
        if(field!=null){
            if(field.equals("subject"))
            {
                field="subject_name_subject_name";
            }}
        model.addAttribute("entities",service.all_questions(offset,field,search));
        if(search!=null){
            model.addAttribute("search",search);
        }
        else{
            model.addAttribute("search","");
        }


        

        if(offset==null) {
                model.addAttribute("currentPage", 1);
            }
            else{
//            System.out.println(offset);
                model.addAttribute("currentPage", offset);
            }
            model.addAttribute("totalPages",(int)Math.ceil(size/(double)10));
            model.addAttribute("pageSize",10);
        return "question_display";
    }
    @RequestMapping("/teacher/question/update_form/{id}")
    public String update_question_form(@PathVariable    int id,Model model,Principal principal){
        model.addAttribute("entites",service.findbyid(Integer.valueOf(id)));
        model.addAttribute("user",principal.getName());
        return "question_update";
    }
    @PostMapping("/teacher/question/update/{id}")
    public String update_question(@RequestBody MultiValueMap<String,String> mp,@PathVariable int id){
        System.out.println(mp);
        questions que=service.findbyid(id);
        que.setQuestion((String)mp.get("question").get(0));
        que.setOption1(mp.get("option1").get(0));
        que.setOption2(mp.get("option2").get(0));
        que.setOption3(mp.get("option3").get(0));
        que.setOption4(mp.get("option4").get(0));
        subject sub = service.getsubbysubname(mp.get("subject").get(0));
        System.out.println(sub);
        if(sub==null){
            que.getSubject_name().setTotal_score(que.getSubject_name().getTotal_score()-que.getScore());
            service.updatesubscore(que.getSubject_name());
            sub=new subject(mp.get("subject").get(0),Integer.valueOf(mp.get("score").get(0)));
            service.save_subject(sub);
        }

        else if(que.getSubject_name()!=sub){
            que.getSubject_name().setTotal_score(que.getSubject_name().getTotal_score()-que.getScore());
            service.updatesubscore(que.getSubject_name());
            sub.setTotal_score(sub.getTotal_score()+Integer.valueOf(mp.get("score").get(0)));
            service.updatesubscore(sub);
        }
        que.setSubject_name(sub);
        que.setCorrectoption(mp.get("Correctoption").get(0));
        que.setScore(Integer.valueOf(mp.get("score").get(0)));
        service.savequestion(que);
        return "redirect:/teacher/allquestions";
    }
    @RequestMapping("/teacher/question/delete/{id}")
    public String deleteque(@PathVariable int id){
        service.deletequestion(id);
        return "redirect:/teacher/allquestions";
    }
    @RequestMapping("/student/history")
    public String history(Principal principal, Model model, @RequestParam Optional<String> field,@RequestParam(required = false) Integer offset,@RequestParam(required = false) Integer dir,@RequestParam(required = false) String search){
        String username=principal.getName();
        model.addAttribute("user", username);
        List<Quiz> quizes=service.quiz_history(username,field,offset,dir,search);

        int size=service.sub_size(username,search);
        System.out.println(Math.ceil(size/(double)10));
        if(dir==null){
            model.addAttribute("dir",dir);
        }
        if(search!=null){
            model.addAttribute("search",search);
        }
        else{
            model.addAttribute("search","");
        }
//        System.out.println(quizes);
        model.addAttribute("entities",quizes);
        if(offset==null) {
            model.addAttribute("currentPage", 1);
        }
        else{
//            System.out.println(offset);
            model.addAttribute("currentPage", offset);
        }
        model.addAttribute("totalPages",Math.ceil(size/(double)10));
        model.addAttribute("pageSize",10);
        if(field.isEmpty()){
            model.addAttribute("field","user_username");
        }
        else{
            model.addAttribute("field",field.get());
        }
        return "quiz_history";
    }


//    @GetMapping("/users")
//    @PreAuthorize("hasRole('ROLE_admin')")
//    private List<User> all(Model model){
//        model.addAttribute(service.all_users());
//        return service.all_users();
//    }
//    @GetMapping("/hello")
//    @ResponseBody
//    public String hello(){
//        return "hello";
//    }

}

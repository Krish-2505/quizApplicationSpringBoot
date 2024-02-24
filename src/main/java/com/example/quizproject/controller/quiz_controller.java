package com.example.quizproject.controller;
import java.time.*;
import com.example.quizproject.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.Session;
import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import com.example.quizproject.service.authservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Controller
public class quiz_controller {
    @Autowired
    private authservice service;
    @RequestMapping("/student/questions/{sub_name}")

    public String show_subject(@PathVariable String sub_name, Model model,HttpServletRequest request,HttpSession session,Principal principal){
        List<question_wrapper> que= service.quebysub(sub_name);
        model.addAttribute("entities",que);
        model.addAttribute("user",principal.getName());
        model.addAttribute("subname",sub_name);
        String name=principal.getName();
        User user=service.userByUsername(name);
        subject sub=service.getsubbysubname(sub_name);

        LocalTime lt=LocalTime.now();
        Calendar c= Calendar.getInstance();
        String s=c.get(Calendar.YEAR)+"/";
        if(c.get(Calendar.MONTH)+1<10){
            s+='0';
        }
        s+=(int)(c.get(Calendar.MONTH)+1)+"/";
        if(c.get(Calendar.DAY_OF_MONTH)+1<10){
            s+='0';
        }
        s+=c.get(Calendar.DAY_OF_MONTH);
        int quiz_id=0;
        if(session.getAttribute(sub_name+user.getUsername())!=null){
            quiz_id=(int)session.getAttribute(sub_name+user.getUsername());

        }

        if(que.size()!=0 && session.getAttribute(sub_name)==null){
        Quiz quiz =new Quiz(user,sub,s+" "+lt.toString());
        service.save_quiz(quiz);
        quiz_id=quiz.getQuiz_id();

         session=request.getSession();
         session.setAttribute(sub_name+user.getUsername(),quiz.getQuiz_id());
        }
        model.addAttribute("quiz_id",quiz_id);

//        session.invalidate();
//        session=request.getSession();



        return "quiz";
    }
    @RequestMapping(value="/student/{sub_name}/{id}/submit_quiz",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    @ResponseBody
    public String submitquiz(@PathVariable String sub_name, @PathVariable int id , @RequestBody MultiValueMap<String, String> mp, Principal principal, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request,HttpSession session){
       System.out.println(mp);
       mp.remove("_csrf");
       int total_score=0;
        int max_score=0;
       for(Map.Entry<String,List<String>> mp1:mp.entrySet()){

            Integer a=Integer.valueOf(mp1.getKey());
            questions que=service.getquebyid(a);
            max_score+=que.getScore();
            if(que.getCorrectoption().equals(mp1.getValue().get(0))){
                total_score+=que.getScore();
            }
        }
        String name=principal.getName();
       User user=service.userByUsername(name);
       subject sub=service.getsubbysubname(sub_name);
       LocalTime lt=LocalTime.now();
        Calendar c= Calendar.getInstance();
        String s=c.get(Calendar.YEAR)+"/";
        s+=(int)(c.get(Calendar.MONTH)+1)+"/";
        s+=c.get(Calendar.DAY_OF_MONTH);
       service.update_quiz_time(id,s+" "+lt.toString(),total_score);
        service.update_user_score(user,sub,total_score);
//        session=request.getSession();
//        session.setAttribute(sub_name,null);
        session.removeAttribute(sub_name+user.getUsername());
//        model.addAttribute("")
        redirectAttributes.addFlashAttribute("max_score",sub.getTotal_score());
        redirectAttributes.addFlashAttribute("score",(int)((total_score/(double)sub.getTotal_score())*100));
        System.out.println((int)((total_score/(double)sub.getTotal_score())*100));
        service.delete_empendtime(user.getUsername());
        return "redirect:/dashboard";
    }
}

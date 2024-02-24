package com.example.quizproject.service;


import com.example.quizproject.model.*;
import com.example.quizproject.repository.*;

import org.hibernate.engine.internal.Cascade;
import org.hibernate.engine.internal.CascadePoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class authservice {
    @Autowired
    private user_score_dao u_dao;
    @Autowired
    private userdao userrepo;
    @Autowired
    private authdao authrepo;
    @Autowired
    private quizdao quizrepo;
    @Autowired
    private questionsdao quedao;
    @Autowired
    private subjectdao subdao;
    public ResponseEntity<String> addStudent(User user) {
        try {
            userrepo.save(user);
            authorities auth=new authorities(user.getUsername(), "ROLE_student");
            auth.setUser(user);
            authrepo.save(auth);
//            user.setRole(auth);


            return new ResponseEntity<>("student added", HttpStatusCode.valueOf(201));
        }
        catch (Exception e){
            User user1=userrepo.findByUsername(user.getUsername());
            if(user1==null) {
                System.out.println("student not saved" + " " + e);
                return new ResponseEntity<>("student not saved", HttpStatus.EXPECTATION_FAILED);
            }
            else{
                return new ResponseEntity<>("student with same username already saved", HttpStatus.EXPECTATION_FAILED);
            }
        }

    }
    public ResponseEntity<String> addUser(User user,String role) {
        try {
            userrepo.save(user);
            authorities auth=new authorities(user.getUsername(), role);
            auth.setUser(user);
            authrepo.save(auth);
//            user.setRole(auth);


            return new ResponseEntity<>("student added", HttpStatusCode.valueOf(201));
        }
        catch (Exception e){
            System.out.println("student not saved"+" "+e);
            return new ResponseEntity<>("student not saved", HttpStatus.EXPECTATION_FAILED);
        }

    }
    public int get_admin_count(String username){
//        System.out.println(authrepo.getadmincount(username));
        return authrepo.getadmincount(username);
    }
    public void saveandupdate(User user,authorities auth){

        userrepo.save(user);
        authrepo.save(auth);
    }
    public Object all_users(Integer offset,String field,String search){
        if(offset==null){
            offset=0;
        }
        else{
            offset=offset-1;
        }
        if(field==null){
            field="username";
        }
        if(search!=null){
            return userrepo.findAll(search,PageRequest.of(offset,10).withSort(Sort.Direction.ASC,field));
        }
        return userrepo.findAll(PageRequest.of(offset,10).withSort(Sort.Direction.ASC,field));
    }
    public User userbyid(int id)
    {
        return userrepo.findById(id).orElse(new User());
    }
    public User userByUsername(String s){
        return userrepo.findByUsername(s);
    }
    public authorities authbyuserid(User user){
        String s = user.getUsername();
        return authrepo.findByUsername(s);


    }
    public List<question_wrapper> quebysub(String subname){

        List<questions> que=quedao.findAllBySubject_name(subname);
        List<question_wrapper> quewrapper=new ArrayList<>();
        for(questions question:que){
            quewrapper.add(new question_wrapper(question.getQ_id(),question.getQuestion(),question.getOption1(),question.getOption2(),question.getOption3(),question.getOption4(),question.getScore()));
        }
        return quewrapper;
    }
    public subject getsubjectscore(String subject_name,int score){
        subject sub=getsubbysubname(subject_name);
        if(sub==null){
            return new subject(subject_name,score);
        }
        score+=sub.getTotal_score();
        System.out.println(score);
        subject sub1=new subject(subject_name,score);
        subdao.updatescore(score,subject_name);
        return sub1;
    }
    public List<questions> quebysub_forcorr(String subname){

        List<questions> que=quedao.findAllBySubject_name(subname);

        return que;
    }
    public void del_by_id(int id){
//        authorities auth1=authrepo.findByUser_id(id);
        try {
            String s = userrepo.findById(id).orElse(new User()).getUsername();
//            System.out.println(s);
            u_dao.deleteAllByUsername(s);
            quizrepo.deleteAllByUsername(s);
            authrepo.deleteByUsername(s);
            userrepo.deleteById(id);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public String get_user_auth(String username){
        authorities auth=authrepo.findByUsername(username);
        if(auth==null){
            return "null";
        }
        return auth.getAuthority().substring(5);

    }
    public int user_size(String search){
        if(search!=null){
            return userrepo.findAll(search).size();
        }
        return userrepo.findAll().size();
    }
    public Object all_quiz(Integer offset, String field,Integer direction,String search) {
        if(offset==null){
            offset=0;
        }
        else{
            offset=offset-1;
        }
        if(field==null){
            field="user_username";
        }
        if(field.equals("subid")){
            field="subid_subject_name";
        }
        if(search!=null){
            if(direction==null || direction==1){
                return quizrepo.findAll(search,PageRequest.of(offset,10).withSort(Sort.Direction.DESC,field));
            }
            return quizrepo.findAll(search,PageRequest.of(offset,10).withSort(Sort.Direction.ASC,field));

        }
        if(direction==null || direction==1){
            return quizrepo.findAll(PageRequest.of(offset,10).withSort(Sort.Direction.DESC,field));
        }
        return quizrepo.findAll(PageRequest.of(offset,10).withSort(Sort.Direction.ASC,field));
    }
    public int sub_size(String search){

        if(search!=null){
            return quizrepo.findAll(search).size();
        }
        return quizrepo.findAll().size();
    }

    public void savequestion(questions que) {

        quedao.save(que);

    }
    public List<questions> all_questions(){
        return quedao.findAll();
    }
    public int get_questions_size(String search){
        if(search!=null){
            return quedao.findAll(search).size();
        }
        return quedao.findAll().size();
    }
     public Object all_questions(Integer offset,String field,String search){
        if(offset==null){
            offset=0;
        }
        else{
            offset--;
        }
        if(search!=null){
            if(field==null){
                return quedao.findAll(search,PageRequest.of(offset, 10));
            }

            return quedao.findAll(search,PageRequest.of(offset, 10).withSort(Sort.Direction.DESC,field));

        }
        if(field==null){
            return quedao.findAll(PageRequest.of(offset, 10));
        }

        return quedao.findAll(PageRequest.of(offset, 10).withSort(Sort.Direction.DESC,field));
    }
    public void deletequestion(int id){
        questions que=quedao.findById(id).orElse(new questions());
        subject sub=que.getSubject_name();
        sub.setTotal_score(sub.getTotal_score()-que.getScore());
        quedao.deleteById(id);
        subdao.updatescore(sub.getTotal_score(),sub.getSubject_name());
//        List<questions> ques=quedao.findAllBySubject_name(sub.getSubject_name());
//        if(ques.size()==0){
//            deletesubject(sub);
//        }
    }
    public void updatesubscore(subject sub){
        subdao.updatescore(sub.getTotal_score(),sub.getSubject_name());
    }
    public void deletesubject(subject sub){
        subdao.delete(sub);
    }
    public questions findbyid(int id){
        return quedao.findById(id).orElse(new questions());
    }
    public subject getsubbysubname(String subject_name){
        return subdao.findBySubject_name(subject_name);
    }
    public int get_user_score(String username){
        System.out.println(u_dao.findAllByUser_username(username));
        if(u_dao.findAllByUser_username(username).size()==0){
            List<subject> sub=subdao.findAll();
           User user=  userrepo.findByUsername(username);
           for(subject sub1:sub){
               user_score usocre=new user_score(user,sub1,0);
               u_dao.save(usocre);
           }

           return 0;

        }
        List<user_score> uscore=u_dao.findAllByUser_username(username);
        int score=0;
        for(user_score u:uscore){
            score+=u.getTotal_score();
        }
        return score;
    }
    public void update_user_score(User user,subject sub,int score){
        user_score userScore=u_dao.findByUser_usernameandsub(user.getUsername(),sub.getSubject_name());
        if(userScore!=null) {
            score = Math.max(score, userScore.getTotal_score());
//        user_score uscore=new user_score(user,score);
            u_dao.updatescore(user.getUsername(), sub.getSubject_name(), score);
        }
        else{
            user_score usocre=new user_score(user,sub,score);
            u_dao.save(usocre);

        }
//        return score;

    }
    public subject save_subject(subject sub) {
        subject sub1=subdao.findBySubject_name(sub.getSubject_name());
        System.out.println(sub1==null);
        if(sub1==null){
        subdao.save(sub);
        return sub;
        }
        return sub1;
    }
    public  void delete_empendtime(String username){
        quizrepo.delete_empendtime(username);
    }
    public List<subject> all_subjects() {
        return subdao.findAll();
    }
    public questions getquebyid(int id){
        return quedao.findById(id).orElse(new questions());
    }

    public void save_quiz(Quiz quiz) {
        quizrepo.save(quiz);
    }
    public int sub_size(String username,String search){

        if(search!=null){
            return quizrepo.search(search,username).size();
        }
        return quizrepo.findAllByUser_username(username).size();
    }
    public List<Quiz> quiz_history(String username, Optional<String> field,Integer offset,Integer dir,String search) {
        System.out.println(username+" "+search);
        if(offset==null){
            offset=1;
        }
       if(!field.isEmpty() && field.get().equals("subid")){
           field=Optional.of("subid_subject_name");
       }

        if(search!=null){
            if(!field.isEmpty()) {
                if (dir == null || dir == 1) {
                    return quizrepo.search(search,username, PageRequest.of(offset - 1, 10).withSort(Sort.by(Sort.Direction.ASC, field.get())));
                }

//            System.out.println(offset+" "+search+" ");
            return quizrepo.search(search,username,PageRequest.of(offset-1,10).withSort(Sort.by(Sort.Direction.DESC,field.get())));}
            if(dir==null||dir==1){
                return quizrepo.search(search,username, PageRequest.of(offset - 1, 10).withSort(Sort.by(Sort.Direction.ASC, "user_username")));
            }
            return quizrepo.search(search,username, PageRequest.of(offset - 1, 10).withSort(Sort.by(Sort.Direction.DESC, "user_username")));

        }
//        System.out.println(offset);
        if(!field.isEmpty()){
            if(dir==null||dir==1){
                return quizrepo.findAllByUser_username(username,PageRequest.of(offset-1,10).withSort(Sort.by(Sort.Direction.ASC,field.get())));
            }
            return quizrepo.findAllByUser_username(username,PageRequest.of(offset-1,10).withSort(Sort.by(Sort.Direction.DESC,field.get())));
        }
        if(dir==null||dir==1){
        return quizrepo.findAllByUser_username(username,PageRequest.of(offset-1,10).withSort(Sort.by(Sort.Direction.ASC, "user_username")));}
        return quizrepo.findAllByUser_username(username,PageRequest.of(offset-1,10).withSort(Sort.by(Sort.Direction.DESC,field.get())));
    }
    public void update_quiz_time(int id,String endtime,int total_score){

        Quiz quiz=quizrepo.findById(id).orElse(null);
        System.out.println(quiz);
        if(quiz!=null){
            quizrepo.updatetimeandscore(id,total_score,endtime);
        }

    }

    public boolean isAdmin(int id) {
       authorities auth= authrepo.findByUser_id(id);
       System.out.println(auth);
       return auth.getAuthority().equals("ROLE_admin");
    }

}

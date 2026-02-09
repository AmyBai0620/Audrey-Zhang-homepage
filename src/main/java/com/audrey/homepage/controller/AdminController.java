package com.audrey.homepage.controller;

import com.audrey.homepage.entity.*;
import com.audrey.homepage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * 后台管理控制器
 * 提供网页界面来管理所有数据
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private EducationService educationService;

    @Autowired
    private ResearchProjectService researchProjectService;

    @Autowired
    private TeachingCourseService teachingCourseService;

    @Autowired
    private AwardService awardService;

    @Autowired
    private ContactInfoService contactInfoService;

    /**
     * 管理首页 - 显示所有教授和统计数据
     */
    @GetMapping
    public String adminHome(Model model) {
        // 获取所有教授
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);

        // 统计数据
        long totalProfessors = professors.size();
        long totalPublications = publicationService.getAllPublications().size();
        long totalProjects = researchProjectService.getAllProjects().size();
        long totalCourses = teachingCourseService.getAllCourses().size();
        long totalAwards = awardService.getAllAwards().size();
        long totalEducations = educationService.getAllEducations().size();

        model.addAttribute("totalProfessors", totalProfessors);
        model.addAttribute("totalPublications", totalPublications);
        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("totalAwards", totalAwards);
        model.addAttribute("totalEducations", totalEducations);

        return "admin/admin-home";
    }

    // ==================== 教授管理 ====================

    /**
     * 教授管理页面
     */
    @GetMapping("/professors")
    public String manageProfessors(Model model) {
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);
        return "admin/professor-management";
    }

    /**
     * 显示添加教授表单
     */
    @GetMapping("/professors/add")
    public String showAddProfessorForm(Model model) {
        model.addAttribute("professor", new Professor());
        return "admin/professor-form";
    }

    /**
     * 显示编辑教授表单
     */
    @GetMapping("/professors/edit/{id}")
    public String showEditProfessorForm(@PathVariable Long id, Model model) {
        Professor professor = professorService.getProfessorById(id)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + id + " 的教授"));
        model.addAttribute("professor", professor);
        return "admin/professor-form";
    }

    /**
     * 保存教授（新增或更新）
     */
    @PostMapping("/professors/save")
    public String saveProfessor(@ModelAttribute Professor professor, RedirectAttributes redirectAttributes) {
        professorService.saveProfessor(professor);
        redirectAttributes.addFlashAttribute("message", "教授信息保存成功！");
        return "redirect:/admin/professors";
    }

    /**
     * 删除教授
     */
    @GetMapping("/professors/delete/{id}")
    public String deleteProfessor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        professorService.deleteProfessor(id);
        redirectAttributes.addFlashAttribute("message", "教授信息删除成功！");
        return "redirect:/admin/professors";
    }

    // ==================== 论文管理 ====================

    /**
     * 论文管理页面
     */
    @GetMapping("/publications")
    public String managePublications(@RequestParam(required = false) Long professorId, Model model) {
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);

        if (professorId != null) {
            List<Publication> publications = publicationService.getPublicationsByProfessorId(professorId);
            model.addAttribute("publications", publications);
            model.addAttribute("selectedProfessorId", professorId);
            professorService.getProfessorById(professorId).ifPresent(p ->
                model.addAttribute("selectedProfessor", p)
            );
        }
        return "admin/publication-management";
    }

    /**
     * 显示添加论文表单
     */
    @GetMapping("/publications/add")
    public String showAddPublicationForm(@RequestParam(required = false) Long professorId, Model model) {
        Publication publication = new Publication();
        if (professorId != null) {
            professorService.getProfessorById(professorId).ifPresent(publication::setProfessor);
        }
        model.addAttribute("publication", publication);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/publication-form";
    }

    /**
     * 显示编辑论文表单
     */
    @GetMapping("/publications/edit/{id}")
    public String showEditPublicationForm(@PathVariable Long id, Model model) {
        Publication publication = publicationService.getPublicationById(id)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + id + " 的论文"));
        model.addAttribute("publication", publication);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/publication-form";
    }

    /**
     * 保存论文（新增或更新）
     */
    @PostMapping("/publications/save")
    public String savePublication(@ModelAttribute Publication publication,
                                 @RequestParam Long professorId,
                                 RedirectAttributes redirectAttributes) {
        // 手动设置Professor对象
        Professor professor = professorService.getProfessorById(professorId)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + professorId + " 的教授"));
        publication.setProfessor(professor);

        publicationService.savePublication(publication);
        redirectAttributes.addFlashAttribute("message", "论文信息保存成功！");
        return "redirect:/admin/publications?professorId=" + professorId;
    }

    /**
     * 删除论文
     */
    @GetMapping("/publications/delete/{id}")
    public String deletePublication(@PathVariable Long id, @RequestParam Long professorId, RedirectAttributes redirectAttributes) {
        publicationService.deletePublication(id);
        redirectAttributes.addFlashAttribute("message", "论文信息删除成功！");
        return "redirect:/admin/publications?professorId=" + professorId;
    }

    // ==================== 教育背景管理 ====================

    @GetMapping("/educations")
    public String manageEducations(@RequestParam(required = false) Long professorId, Model model) {
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);
        if (professorId != null) {
            List<Education> educations = educationService.getEducationsByProfessorId(professorId);
            model.addAttribute("educations", educations);
            model.addAttribute("selectedProfessorId", professorId);
            professorService.getProfessorById(professorId).ifPresent(p ->
                    model.addAttribute("selectedProfessor", p));
        }
        return "admin/education-management";
    }

    @GetMapping("/educations/add")
    public String showAddEducationForm(@RequestParam(required = false) Long professorId, Model model) {
        Education education = new Education();
        if (professorId != null) {
            professorService.getProfessorById(professorId).ifPresent(education::setProfessor);
        }
        model.addAttribute("education", education);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/education-form";
    }

    @GetMapping("/educations/edit/{id}")
    public String showEditEducationForm(@PathVariable Long id, Model model) {
        Education education = educationService.getEducationById(id)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + id + " 的教育背景"));
        model.addAttribute("education", education);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/education-form";
    }

    @PostMapping("/educations/save")
    public String saveEducation(@ModelAttribute Education education,
                               @RequestParam Long professorId,
                               RedirectAttributes redirectAttributes) {
        // 手动设置Professor对象
        Professor professor = professorService.getProfessorById(professorId)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + professorId + " 的教授"));
        education.setProfessor(professor);

        educationService.saveEducation(education);
        redirectAttributes.addFlashAttribute("message", "教育背景保存成功！");
        return "redirect:/admin/educations?professorId=" + professorId;
    }

    @GetMapping("/educations/delete/{id}")
    public String deleteEducation(@PathVariable Long id, @RequestParam Long professorId, RedirectAttributes redirectAttributes) {
        educationService.deleteEducation(id);
        redirectAttributes.addFlashAttribute("message", "教育背景删除成功！");
        return "redirect:/admin/educations?professorId=" + professorId;
    }

    // ==================== 科研项目管理 ====================

    @GetMapping("/research-projects")
    public String manageResearchProjects(@RequestParam(required = false) Long professorId, Model model) {
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);
        if (professorId != null) {
            List<ResearchProject> projects = researchProjectService.getResearchProjectsByProfessorId(professorId);
            model.addAttribute("projects", projects);
            model.addAttribute("selectedProfessorId", professorId);
            professorService.getProfessorById(professorId).ifPresent(p ->
                    model.addAttribute("selectedProfessor", p));
        }
        return "admin/research-project-management";
    }

    @GetMapping("/research-projects/add")
    public String showAddResearchProjectForm(@RequestParam(required = false) Long professorId, Model model) {
        ResearchProject project = new ResearchProject();
        if (professorId != null) {
            professorService.getProfessorById(professorId).ifPresent(project::setProfessor);
        }
        model.addAttribute("project", project);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/research-project-form";
    }

    @GetMapping("/research-projects/edit/{id}")
    public String showEditResearchProjectForm(@PathVariable Long id, Model model) {
        ResearchProject project = researchProjectService.getResearchProjectById(id)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + id + " 的科研项目"));
        model.addAttribute("project", project);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/research-project-form";
    }

    @PostMapping("/research-projects/save")
    public String saveResearchProject(@ModelAttribute ResearchProject project,
                                     @RequestParam Long professorId,
                                     RedirectAttributes redirectAttributes) {
        // 手动设置Professor对象
        Professor professor = professorService.getProfessorById(professorId)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + professorId + " 的教授"));
        project.setProfessor(professor);

        researchProjectService.saveResearchProject(project);
        redirectAttributes.addFlashAttribute("message", "科研项目保存成功！");
        return "redirect:/admin/research-projects?professorId=" + professorId;
    }

    @GetMapping("/research-projects/delete/{id}")
    public String deleteResearchProject(@PathVariable Long id, @RequestParam Long professorId, RedirectAttributes redirectAttributes) {
        researchProjectService.deleteResearchProject(id);
        redirectAttributes.addFlashAttribute("message", "科研项目删除成功！");
        return "redirect:/admin/research-projects?professorId=" + professorId;
    }

    // ==================== 教学课程管理 ====================

    @GetMapping("/teaching-courses")
    public String manageTeachingCourses(@RequestParam(required = false) Long professorId, Model model) {
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);
        if (professorId != null) {
            List<TeachingCourse> courses = teachingCourseService.getTeachingCoursesByProfessorId(professorId);
            model.addAttribute("courses", courses);
            model.addAttribute("selectedProfessorId", professorId);
            professorService.getProfessorById(professorId).ifPresent(p ->
                    model.addAttribute("selectedProfessor", p));
        }
        return "admin/teaching-course-management";
    }

    @GetMapping("/teaching-courses/add")
    public String showAddTeachingCourseForm(@RequestParam(required = false) Long professorId, Model model) {
        TeachingCourse course = new TeachingCourse();
        if (professorId != null) {
            professorService.getProfessorById(professorId).ifPresent(course::setProfessor);
        }
        model.addAttribute("course", course);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/teaching-course-form";
    }

    @GetMapping("/teaching-courses/edit/{id}")
    public String showEditTeachingCourseForm(@PathVariable Long id, Model model) {
        TeachingCourse course = teachingCourseService.getTeachingCourseById(id)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + id + " 的教学课程"));
        model.addAttribute("course", course);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/teaching-course-form";
    }

    @PostMapping("/teaching-courses/save")
    public String saveTeachingCourse(@ModelAttribute TeachingCourse course,
                                    @RequestParam Long professorId,
                                    RedirectAttributes redirectAttributes) {
        // 手动设置Professor对象
        Professor professor = professorService.getProfessorById(professorId)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + professorId + " 的教授"));
        course.setProfessor(professor);

        teachingCourseService.saveTeachingCourse(course);
        redirectAttributes.addFlashAttribute("message", "教学课程保存成功！");
        return "redirect:/admin/teaching-courses?professorId=" + professorId;
    }

    @GetMapping("/teaching-courses/delete/{id}")
    public String deleteTeachingCourse(@PathVariable Long id, @RequestParam Long professorId, RedirectAttributes redirectAttributes) {
        teachingCourseService.deleteTeachingCourse(id);
        redirectAttributes.addFlashAttribute("message", "教学课程删除成功！");
        return "redirect:/admin/teaching-courses?professorId=" + professorId;
    }

    // ==================== 获奖荣誉管理 ====================

    @GetMapping("/awards")
    public String manageAwards(@RequestParam(required = false) Long professorId, Model model) {
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);
        if (professorId != null) {
            List<Award> awards = awardService.getAwardsByProfessorId(professorId);
            model.addAttribute("awards", awards);
            model.addAttribute("selectedProfessorId", professorId);
            professorService.getProfessorById(professorId).ifPresent(p ->
                    model.addAttribute("selectedProfessor", p));
        }
        return "admin/award-management";
    }

    @GetMapping("/awards/add")
    public String showAddAwardForm(@RequestParam(required = false) Long professorId, Model model) {
        Award award = new Award();
        if (professorId != null) {
            professorService.getProfessorById(professorId).ifPresent(award::setProfessor);
        }
        model.addAttribute("award", award);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/award-form";
    }

    @GetMapping("/awards/edit/{id}")
    public String showEditAwardForm(@PathVariable Long id, Model model) {
        Award award = awardService.getAwardById(id)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + id + " 的获奖信息"));
        model.addAttribute("award", award);
        model.addAttribute("professors", professorService.getAllProfessors());
        return "admin/award-form";
    }

    @PostMapping("/awards/save")
    public String saveAward(@ModelAttribute Award award,
                           @RequestParam Long professorId,
                           RedirectAttributes redirectAttributes) {
        // 手动设置Professor对象
        Professor professor = professorService.getProfessorById(professorId)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + professorId + " 的教授"));
        award.setProfessor(professor);

        awardService.saveAward(award);
        redirectAttributes.addFlashAttribute("message", "获奖信息保存成功！");
        return "redirect:/admin/awards?professorId=" + professorId;
    }

    @GetMapping("/awards/delete/{id}")
    public String deleteAward(@PathVariable Long id, @RequestParam Long professorId, RedirectAttributes redirectAttributes) {
        awardService.deleteAward(id);
        redirectAttributes.addFlashAttribute("message", "获奖信息删除成功！");
        return "redirect:/admin/awards?professorId=" + professorId;
    }

    // ==================== 联系方式管理 ====================

    @GetMapping("/contact-info")
    public String manageContactInfo(@RequestParam(required = false) Long professorId, Model model) {
        List<Professor> professors = professorService.getAllProfessors();
        model.addAttribute("professors", professors);
        if (professorId != null) {
            Optional<ContactInfo> contactInfo = contactInfoService.getContactInfoByProfessorId(professorId);
            model.addAttribute("contactInfo", contactInfo.orElse(null));
            model.addAttribute("selectedProfessorId", professorId);
            professorService.getProfessorById(professorId).ifPresent(p ->
                    model.addAttribute("selectedProfessor", p));
        }
        return "admin/contact-info-management";
    }

    @GetMapping("/contact-info/edit")
    public String showEditContactInfoForm(@RequestParam Long professorId, Model model) {
        Professor professor = professorService.getProfessorById(professorId)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + professorId + " 的教授"));

        ContactInfo contactInfo = contactInfoService.getContactInfoByProfessorId(professorId)
                .orElseGet(() -> {
                    ContactInfo newContactInfo = new ContactInfo();
                    newContactInfo.setProfessor(professor);
                    return newContactInfo;
                });

        model.addAttribute("contactInfo", contactInfo);
        model.addAttribute("professor", professor);
        return "admin/contact-info-form";
    }

    @PostMapping("/contact-info/save")
    public String saveContactInfo(@ModelAttribute ContactInfo contactInfo,
                                  @RequestParam Long professorId,
                                  RedirectAttributes redirectAttributes) {
        // 手动设置Professor对象
        Professor professor = professorService.getProfessorById(professorId)
                .orElseThrow(() -> new RuntimeException("找不到ID为 " + professorId + " 的教授"));
        contactInfo.setProfessor(professor);

        contactInfoService.saveContactInfo(contactInfo);
        redirectAttributes.addFlashAttribute("message", "联系方式保存成功！");
        return "redirect:/admin/contact-info?professorId=" + professorId;
    }
}

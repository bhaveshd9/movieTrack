package test.Announcement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class AnnouncementController {

    @Autowired
    AnnouncementRepository announcementRepository;

    @GetMapping("/ann/all")
    List<Announcement> getAllAnnouncements(){
        return announcementRepository.findAll();
    }

    @GetMapping("/ann/{id}")
    Announcement getAnnouncementById(@PathVariable long id){
        if(announcementRepository.findById(id) != null){
            return announcementRepository.findById(id);
        } else {
            return null;
        }
    }

    @PostMapping("/ann/newString/{title}/{newText}")
    String postNewAnnouncementByString(@PathVariable String title, @PathVariable String newText){
        Announcement newAnn = new Announcement();
        newAnn.setTitle(title);
        newAnn.setDescription(newText);
        announcementRepository.save(newAnn);
        return "Successfully created a new announcement";
    }

    @PostMapping("/ann/newPost")
    String postNewAnnouncementByObj(@RequestBody Announcement announcement){
        if(announcement != null && announcementRepository.findById(announcement.getId()) == null) {
            announcementRepository.save(announcement);
            return "Successfully created a new announcement";
        } else {
            return "Failed to create a new announcement";
        }
    }

    @PutMapping("/ann/editText/{id}/{newText}")
    String putTextByString(@PathVariable long id, @PathVariable String newText){
        if(announcementRepository.findById(id) != null){
            Announcement editedAnnouncement = announcementRepository.findById(id);
            editedAnnouncement.setDescription(newText);
            editedAnnouncement.setDate(LocalDate.now());
            editedAnnouncement.setTime(LocalTime.now());
            announcementRepository.save(editedAnnouncement);
            return "Successfully edited an announcement";
        } else {
            return "Failed to edit announcement";
        }
    }

    @PutMapping("/ann/editTitle/{id}/{newTitle}")
    String putTitleByString(@PathVariable long id, @PathVariable String newTitle){
        if(announcementRepository.findById(id) != null){
            Announcement editedAnnouncement = announcementRepository.findById(id);
            editedAnnouncement.setTitle(newTitle);
            editedAnnouncement.setDate(LocalDate.now());
            editedAnnouncement.setTime(LocalTime.now());
            announcementRepository.save(editedAnnouncement);
            return "Successfully edited an announcement";
        } else {
            return "Failed to edit announcement";
        }
    }

    @DeleteMapping ("/ann/delete/{id}")
    String deleteAnnouncement(@PathVariable long id){
        if(announcementRepository.findById(id) != null){
            announcementRepository.delete(announcementRepository.findById(id));
            return "Successfully deleted an announcement";
        } else {
            return "Failed to delete announcement";
        }
    }

    @DeleteMapping("/ann/delete/all")
    String deleteAllAnnouncement(){
        announcementRepository.deleteAll();
        return "Successfully deleted all announcements";
    }

}
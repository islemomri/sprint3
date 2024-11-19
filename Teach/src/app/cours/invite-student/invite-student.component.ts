import { Component } from '@angular/core';
import { CoursService } from '../service/cours.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-invite-student',
  templateUrl: './invite-student.component.html',
  styleUrls: ['./invite-student.component.css']
})
export class InviteStudentComponent {
  submitted = false;

  constructor(private service: CoursService, private router: Router) {}

  onSubmit(f: NgForm) {
    this.submitted = true;

    if (f.invalid) {
      return;
    } else {
      const courseCode = f.value.courseCode;
      const studentId = f.value.studentId;
      const studentEmail = f.value.studentEmail;

      if (studentId) {
        this.service.inviteStudentById(courseCode, studentId).subscribe(
          response => {
            console.log('Invitation sent successfully:', response);
            this.router.navigate(['/cours']);
          },
          error => {
            console.error('Error sending invitation:', error);
          }
        );
      } else if (studentEmail) {
        this.service.inviteStudentByEmail(courseCode, studentEmail).subscribe(
          response => {
            console.log('Invitation sent successfully:', response);
            this.router.navigate(['/cours']);
          },
          error => {
            console.error('Error sending invitation:', error);
          }
        );
      }
    }
  }
}

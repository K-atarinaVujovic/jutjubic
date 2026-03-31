import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { VideoService } from '../../service/video.service';


@Component({
  selector: 'app-schedule-video-prompt',
  templateUrl: './schedule-video-prompt.component.html',
  styleUrls: ['./schedule-video-prompt.component.css']
})
export class ScheduleVideoPromptComponent implements OnInit {
  @Input() formData: FormData = new FormData();
  @Output() close = new EventEmitter<void>();
  showMessage: boolean = false;
  message: string = "";

  constructor(
    private fb: FormBuilder,
    private videoService: VideoService,
  ) {
  }

  ngOnInit(): void {
    this.logFormData();
    this.showMessage = false;
  }
  
  scheduleForm = this.fb.group({
    date: [null, Validators.required],
    time: [null, Validators.required],
  });

  private logFormData(){
  this.formData.forEach((value, key) => {
        console.log(key, value);
      });
  }

  publishImmediately(): void {
    this.uploadVideo();
  }

  publishScheduled(): void {
    if (this.scheduleForm.invalid) {
      this.setMessage("Invalid form!!");
      return;
    }

    const date = this.scheduleForm.value.date || Date();
    const time = this.scheduleForm.value.time || "";
    const [hours, minutes] = time.split(':');
    const combined = new Date(date);
    combined.setHours(+hours, +minutes, 0, 0);

    if (combined <= new Date()) {
      this.setMessage("Scheduled time must be in the future!");
      return;
    }

    const scheduledAt = this.convertToLocalDateTimeFormat(combined);
    console.log("SCHEDULEEEDDED: " + scheduledAt);
    this.formData.append('scheduledAt', scheduledAt);
    this.uploadVideo();
  }

  closePrompt(): void{
    this.close.emit();
  }

  private convertToLocalDateTimeFormat(date: Date): string{
    const pad = (n: number) => n.toString().padStart(2, '0');
    const scheduledAt = `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}:00`;

    return scheduledAt;
  }

  private setMessage(text: string){
    this.message = text;
    this.showMessage = true;
  }

  private uploadVideo(){
    this.videoService.uploadVideo(this.formData).subscribe({
      next: (result) => {
        this.logFormData();
        this.setMessage(`${this.formData.get("title")} successfully uploaded!`);
      },
      error: (err) =>{
        this.setMessage(`Error!! :  ${err.error || err.message}`);;
      }
    });
  }
}

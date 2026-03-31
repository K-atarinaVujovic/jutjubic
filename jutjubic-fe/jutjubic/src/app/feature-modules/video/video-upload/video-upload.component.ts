import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators} from '@angular/forms';
import { UtilsService } from 'src/app/shared/utils.service';
import { VideoService } from '../service/video.service';

@Component({
  selector: 'app-video-upload',
  templateUrl: './video-upload.component.html',
  styleUrls: ['./video-upload.component.css']
})
export class VideoUploadComponent {
  selectedThumbnailFile: File | null = null;
  thumbnailPreview: string | ArrayBuffer | null = "";
  videoPreview: string | ArrayBuffer | null = "";
  selectedVideoFile: File | null = null;
  showError: boolean = true;
  errorMessage: string = "";
  formData: FormData = new FormData();
  showSchedulePrompt: boolean = false;

  uploadForm = new FormGroup({
    title: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    tags: new FormControl('', [Validators.required]),
    location: new FormControl('', [Validators.required]),
  });

  constructor(
    private utilsService: UtilsService,
    private videoService: VideoService,
  ) {}

  publishVideo(): void {
    this.showError = false;
    this.errorMessage = "";
    if(this.uploadForm.invalid){
      this.uploadForm.markAllAsTouched();
      this.errorMessage = "Invalid form data";
      this.showError = true;
      return;
    }

    this.formData.append('title', this.uploadForm.value.title!);
    this.formData.append('description', this.uploadForm.value.description!);
    this.formData.append('location', this.uploadForm.value.location!);
    
    const tags = this.uploadForm.value.tags?.split(',').map(t => t.trim()) ?? [];
    tags.forEach(tag => this.formData.append('tags', tag));

    this.formData.append('thumbnail', this.selectedThumbnailFile!);
    this.formData.append('videoFile', this.selectedVideoFile!);
  
    this.showSchedulePrompt = true;
  }

  private setMessage(text: string){
    this.errorMessage = text;
    this.showError = true;
  }

  onThumbnailSelected(event: any){
    const input = event.target as HTMLInputElement;
    if(input.files){
      this.selectedThumbnailFile = input.files[0];
      this.utilsService.loadImagePreview(input.files[0])
      .then(result => {
        this.thumbnailPreview = result;
      })
      .catch(err => {
        console.error('Failed to load image preview', err);
      });
    }
  }

  onVideoSelected(event: any) {
    const input = event.target as HTMLInputElement;
    if (input.files![0].size > 200 * 1024 * 1024) {
        this.setMessage('Video exceeds maximum allowed size of 200MB!!');
        return;
      }
    if (input.files) {
      this.selectedVideoFile = input.files[0];
      this.videoPreview = URL.createObjectURL(input.files[0]);
    }
  }
}

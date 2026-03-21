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

    const formData = new FormData();
    formData.append('title', this.uploadForm.value.title!);
    formData.append('description', this.uploadForm.value.description!);
    formData.append('location', this.uploadForm.value.location!);
    
    const tags = this.uploadForm.value.tags?.split(',').map(t => t.trim()) ?? [];
    tags.forEach(tag => formData.append('tags', tag));

    formData.append('thumbnail', this.selectedThumbnailFile!);
    formData.append('videoFile', this.selectedVideoFile!);

    this.videoService.uploadVideo(formData).subscribe();
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
  if (input.files) {
    this.selectedVideoFile = input.files[0];
    this.videoPreview = URL.createObjectURL(input.files[0]);
  }
}
}

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {

  constructor() { }

  loadImagePreview(file: File): Promise<string | ArrayBuffer> {
    return new Promise((resolve, reject) => {
      if (!file) return reject('No file provided');

      const reader = new FileReader();
      reader.onload = () => resolve(reader.result!);
      reader.onerror = (err) => reject(err);
      reader.readAsDataURL(file);
    });
  }
}

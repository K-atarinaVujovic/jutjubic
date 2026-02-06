import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Video } from '../model/video.model';

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  private readonly baseUrl = 'http://localhost:8080/api/videos';

  constructor(private http: HttpClient) {}

  getAll(page: number, size: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<any>(`${this.baseUrl}/all`, { params });
  }

  getAllSorted(): Observable<Video[]> {
    return this.http.get<Video[]>(`${this.baseUrl}/all-sorted`);
  }

  getById(id: number): Observable<Video> {
    return this.http.get<Video>(`${this.baseUrl}/${id}`);
  }

  getThumbnail(path: string): Observable<Blob> {
    const params = new HttpParams().set('path', path);
    return this.http.get(`${this.baseUrl}/thumbnail`, {
      params,
      responseType: 'blob'
    });
  }

  uploadVideo(formData: FormData): Observable<Video> {
    return this.http.post<Video>(`${this.baseUrl}/upload`, formData);
  }

  getComments(videoId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/${videoId}/comments`);
  }

  postComment(videoId: number, userId: number, text: string): Observable<any> {
    const params = new HttpParams()
      .set('userId', userId)
      .set('text', text);

    return this.http.post(`${this.baseUrl}/${videoId}/comments`, null, { params });
  }

  like(videoId: number, userId: number): Observable<string> {
    const params = new HttpParams().set('userId', userId);
    return this.http.post(`${this.baseUrl}/${videoId}/likes`, null, {
      params,
      responseType: 'text'
    });
  }

  getLikes(videoId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/${videoId}/likes`);
  }
}
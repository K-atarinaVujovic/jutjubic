
export interface Video {
  id: number;
  title: string;
  description: string;
  tags: string[];
  thumbnailUrl: string;
  videoUrl: string;
  dateCreated?: string;
  location: string;
  viewCount: number;
  live: boolean;
  forRegularViewing: boolean;
  scheduledAt: string;
}

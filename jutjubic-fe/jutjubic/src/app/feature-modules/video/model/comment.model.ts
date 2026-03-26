import { User } from "../../user/model/user.model";
import { Video } from "./video.model";

export interface Comment{
  id: number;
  video: Video;
  user: User;
  text: string;
  dateCreated?: string; 
}
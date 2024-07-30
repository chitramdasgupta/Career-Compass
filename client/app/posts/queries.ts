import axios from "axios";
import { Post } from "./types";

const POSTS_URL = "http://localhost:8080/posts/";

export async function fetchPosts(): Promise<Post[]> {
  try {
    const response = await axios.get<Post[]>(POSTS_URL);
    return response.data;
  } catch (error) {
    console.error("Error fetching posts:", error);
    throw error;
  }
}
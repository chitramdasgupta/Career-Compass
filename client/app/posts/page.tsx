"use client";

import React, { useEffect, useState } from "react";
import { Post } from "./types";
import { fetchPosts } from "./postsApi";
import PostList from "./components/PostList";

export default function Posts() {
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetchPosts()
      .then((fetchedPosts) => {
        setPosts(fetchedPosts);
      })
      .catch((error: Error) => {
        console.error("Error fetching posts:", error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <div>
      <h1 className="my-2">Posts</h1>
      <PostList posts={posts} loading={loading} />
    </div>
  );
}

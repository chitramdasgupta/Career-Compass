import React from "react";
import PostItem from "./../Post";
import { Post } from "../../types";
import { SkeletonList } from "../PostSkeleton/skeletonList";

interface PostListProps {
  posts: Post[];
  loading: boolean;
}

const PostList: React.FC<PostListProps> = ({ posts, loading }) => {
  if (loading) {
    return <SkeletonList count={6} />;
  }

  return (
    <>
      {posts.map((post) => (
        <PostItem post={post} key={post.id} />
      ))}
    </>
  );
};

export default PostList;

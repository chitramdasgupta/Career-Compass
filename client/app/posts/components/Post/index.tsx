import React from "react";
import { Post } from "../../types";
import ExpandableContent from "../ExpandableContent";

interface PostProps {
  post: Post;
}

const PostItem: React.FC<PostProps> = ({ post }) => {
  const CONTENT_PREVIEW_LENGTH = 150;

  return (
    <div className="border-2 mb-3 px-2 py-1 rounded-lg">
      <div className="font-bold">{post.title}</div>
      <ExpandableContent
        content={post.content}
        previewLength={CONTENT_PREVIEW_LENGTH}
      />
    </div>
  );
};

export default PostItem;

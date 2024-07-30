import React, { useState } from "react";
import ToggleButton from "../ToggleButton";

interface ExpandableContentProps {
  content: string | null;
  previewLength: number;
}

const ExpandableContent: React.FC<ExpandableContentProps> = ({
  content,
  previewLength,
}) => {
  const [isExpanded, setIsExpanded] = useState(false);

  if (!content) return null;

  const showReadMore = content.length > previewLength;
  const displayText = isExpanded ? content : content.slice(0, previewLength);

  const toggleReadMore = () => {
    setIsExpanded(!isExpanded);
  };

  return (
    <div>
      <div>
        {displayText}
        {!isExpanded && showReadMore && "..."}
      </div>
      {showReadMore && (
        <ToggleButton isExpanded={isExpanded} onClick={toggleReadMore} />
      )}
    </div>
  );
};

export default ExpandableContent;

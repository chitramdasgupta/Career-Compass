import React from "react";

interface ToggleButtonProps {
  isExpanded: boolean;
  onClick: () => void;
}

const ToggleButton: React.FC<ToggleButtonProps> = ({ isExpanded, onClick }) => {
  return (
    <button onClick={onClick} className="text-gray-500 hover:text-gray-600">
      {isExpanded ? "Read less" : "Read more..."}
    </button>
  );
};

export default ToggleButton;

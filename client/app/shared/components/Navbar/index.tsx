"use client";

import React, { useState } from "react";
import MenuIcon from "@mui/icons-material/Menu";
import Link from "next/link";
import ExploreOutlinedIcon from "@mui/icons-material/ExploreOutlined";
import CloseIcon from "@mui/icons-material/Close";

export default function Navbar() {
  const NAVBAR_HEIGHT = "2rem";
  const NAVBAR_ITEMS = ["Posts", "Jobs", "Companies"];

  const [isMenuOpen, setIsMenuOpen]: [
    boolean,
    (value: ((prevState: boolean) => boolean) | boolean) => void,
  ] = useState(false);

  function toggleMenu() {
    setIsMenuOpen(!isMenuOpen);
  }

  return (
    <nav className="bg-blue-950 text-white py-2">
      <div
        className={`lg:container lg:mx-auto flex px-1 justify-between items-center h-[${NAVBAR_HEIGHT}]`}
      >
        <Link href="/">
          <span>
            Career <ExploreOutlinedIcon />
          </span>
        </Link>
        <div
          className={`${isMenuOpen ? "flex" : "hidden"} md:flex absolute md:static bg-blue-950 left-0 top-[${NAVBAR_HEIGHT}] w-full justify-center text-center md:w-auto`}
        >
          <ul className="md:flex md:gap-3">
            {NAVBAR_ITEMS.map((navItem, index) => (
              <li
                className="pb-1 hover:cursor-pointer hover:text-gray-400 md:pb-0"
                key={index}
              >
                <Link href={`/${navItem.toLowerCase()}`} onClick={toggleMenu}>
                  {navItem}
                </Link>
              </li>
            ))}
          </ul>
        </div>
        {isMenuOpen ? (
          <CloseIcon
            onClick={toggleMenu}
            className="hover:cursor-pointer md:!hidden"
          />
        ) : (
          <MenuIcon
            onClick={toggleMenu}
            className="hover:cursor-pointer md:!hidden"
          />
        )}
      </div>
    </nav>
  );
}

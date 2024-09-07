"use client";

import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Menu from "@mui/material/Menu";
import MenuIcon from "@mui/icons-material/Menu";
import Avatar from "@mui/material/Avatar";
import MenuItem from "@mui/material/MenuItem";
import Link from "next/link";
import ExploreOutlinedIcon from "@mui/icons-material/ExploreOutlined";
import Button from "@mui/material/Button";
import { Container } from "@mui/material";
import { useRouter } from "next/navigation";
import { useLogoutUserMutation } from "@/app/auth/authApi";
import CustomSnackbar from "@/app/shared/components/CustomSnackbar";
import { Severity, SnackbarMessage } from "../CustomSnackbar/types";
import { useAuth } from "../../hooks/useAuth";

const pages = ["Jobs", "Companies"];

function AppNavbar() {
  const router = useRouter();
  const { isAuthenticated, isCandidate, isCompany, user } = useAuth();
  const [logoutUser, { isLoading }] = useLogoutUserMutation();
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(
    null,
  );
  const [anchorElMenu, setAnchorElMenu] = React.useState<null | HTMLElement>(
    null,
  );
  const [snackbarMessage, setSnackbarMessage] =
    React.useState<SnackbarMessage | null>(null);

  const handleUserMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleUserMenuClose = () => {
    setAnchorElUser(null);
  };

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElMenu(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorElMenu(null);
  };

  const handleLogout = async () => {
    try {
      await logoutUser().unwrap();

      handleUserMenuClose();

      router.push("/auth/login");

      setSnackbarMessage({
        message: "Logged out successfully",
        severity: Severity.Success,
      });
    } catch (error) {
      setSnackbarMessage({
        message: "Logout failed. Please try again.",
        severity: Severity.Error,
      });
    }
  };

  return (
    <AppBar position="static">
      <Container maxWidth="xl">
        <Toolbar>
          <Link
            href="/"
            style={{
              marginRight: "auto",
              display: "flex",
              alignItems: "center",
              textDecoration: "none",
              fontSize: "1.5rem",
              color: "inherit",
            }}
          >
            Career{" "}
            <ExploreOutlinedIcon
              sx={{ marginLeft: "4px", marginTop: "4px", fontSize: "inherit" }}
            />
          </Link>
          {/* Desktop menu items */}
          <Box sx={{ display: { xs: "none", md: "flex" }, marginLeft: "auto" }}>
            {pages.map((page) => (
              <Button
                key={page}
                component={Link}
                href={`/${page.toLowerCase()}`}
                sx={{ color: "white", display: "block" }}
              >
                {page}
              </Button>
            ))}
          </Box>
          {/* Mobile menu */}
          <Box sx={{ display: { xs: "flex", md: "none" } }}>
            <IconButton size="large" onClick={handleMenuOpen} color="inherit">
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElMenu}
              anchorOrigin={{
                vertical: "bottom",
                horizontal: "left",
              }}
              keepMounted
              transformOrigin={{
                vertical: "top",
                horizontal: "left",
              }}
              open={Boolean(anchorElMenu)}
              onClose={handleMenuClose}
              sx={{
                display: { xs: "block", md: "none" },
              }}
            >
              {pages.map((page) => (
                <MenuItem key={page} onClick={handleMenuClose}>
                  <Link href={`/${page.toLowerCase()}`}>{page}</Link>
                </MenuItem>
              ))}
            </Menu>
          </Box>
          {/* User menu */}
          {isAuthenticated ? (
            <Box>
              <IconButton onClick={handleUserMenuOpen} color="inherit">
                <Avatar>G</Avatar>
              </IconButton>
              <Menu
                sx={{ mt: "45px" }}
                id="user-menu-appbar"
                anchorEl={anchorElUser}
                anchorOrigin={{
                  vertical: "top",
                  horizontal: "right",
                }}
                keepMounted
                transformOrigin={{
                  vertical: "top",
                  horizontal: "right",
                }}
                open={Boolean(anchorElUser)}
                onClose={handleUserMenuClose}
              >
                <MenuItem onClick={handleUserMenuClose}>
                  <Link href="/profile">Profile</Link>
                </MenuItem>
                <MenuItem onClick={handleUserMenuClose}>
                  <Link href="/dashboard">Dashboard</Link>
                </MenuItem>
                <MenuItem onClick={handleLogout}>Log out</MenuItem>
              </Menu>
            </Box>
          ) : (
            <Button color="inherit" component={Link} href="/auth/login">
              Login
            </Button>
          )}
        </Toolbar>
      </Container>
      <CustomSnackbar snackbarMessage={snackbarMessage} />
    </AppBar>
  );
}

export default AppNavbar;

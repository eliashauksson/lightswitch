# Lightswitch

A script to toggle dark and light theme on my system.

Supported Apps:

- [Emacs](https://www.gnu.org/software/emacs/)
- [Kitty](https://sw.kovidgoyal.net/kitty/)
- [Polybar](https://polybar.github.io/)
- [Rofi](https://github.com/davatorium/rofi)
- [Dunst](https://dunst-project.org/)

## Install

**Warning** This script has a lot of custom paths and lines which are correct for my personal system. You probably have to change them to match your configurations.

To get started run

```
git clone https://github.com/eliashauksson/lightswitch
cd lightswitch
bash install.sh
```

Then map a keybinding to the following command (here sxhkd)

```
super + alt + c
	bb ~/.local/share/lightswitch/switch-theme.clj
```

For Kitty to work add these lines to the conf

```
allow_remote_control yes
listen_on unix:/tmp/kitty
```

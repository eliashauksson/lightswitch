# Lightswitch

A script to toggle dark and light theme on my system.

Supported Apps:

- Emacs

## Install

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

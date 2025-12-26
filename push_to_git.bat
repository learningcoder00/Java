@echo off
chcp 65001 >nul
cd /d "%~dp0"
echo ========================================
echo Git 提交和推送脚本
echo ========================================
echo.
echo [1/4] 检查 Git 状态...
git status
echo.
echo [2/4] 添加所有修改的文件...
git add .
echo.
echo [3/4] 提交修改...
git commit -m "优化导航栏布局：固定用户信息在右侧，调整下拉菜单宽度与菜单项一致"
echo.
echo [4/4] 推送到远程仓库...
git push
echo.
echo ========================================
echo 完成！
echo ========================================
pause


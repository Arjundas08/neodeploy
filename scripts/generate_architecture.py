"""
NeoDeploy - System Architecture Diagram Generator
Creates a professional architecture image
"""

import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch, Circle
import numpy as np

# Create figure with dark background
fig, ax = plt.subplots(1, 1, figsize=(16, 10), facecolor='#0f172a')
ax.set_facecolor('#0f172a')
ax.set_xlim(0, 16)
ax.set_ylim(0, 10)
ax.axis('off')

# Colors
BLUE = '#3b82f6'
GREEN = '#22c55e'
PURPLE = '#a855f7'
ORANGE = '#f97316'
CYAN = '#06b6d4'
PINK = '#ec4899'
YELLOW = '#eab308'
WHITE = '#ffffff'
GRAY = '#94a3b8'
DARK = '#1e293b'

def draw_box(x, y, width, height, color, text, icon='', fontsize=11):
    """Draw a rounded rectangle box with text"""
    box = FancyBboxPatch((x, y), width, height, 
                          boxstyle="round,pad=0.05,rounding_size=0.15",
                          facecolor=color, edgecolor='white', linewidth=2)
    ax.add_patch(box)
    
    # Add text
    display_text = f"{icon}\n{text}" if icon else text
    ax.text(x + width/2, y + height/2, display_text, 
            ha='center', va='center', fontsize=fontsize, 
            color='white', fontweight='bold', linespacing=1.5)

def draw_arrow(x1, y1, x2, y2, color='white'):
    """Draw an arrow between two points"""
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', color=color, lw=2.5))

def draw_dashed_arrow(x1, y1, x2, y2, color='white'):
    """Draw a dashed arrow"""
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', color=color, lw=2, ls='--'))

# ==========================================
# Title
# ==========================================
ax.text(8, 9.5, '🚀 NeoDeploy - System Architecture', 
        ha='center', va='center', fontsize=24, color=WHITE, fontweight='bold')

ax.text(8, 9.0, 'CI/CD Pipeline with Self-Healing & Real-Time Monitoring', 
        ha='center', va='center', fontsize=12, color=GRAY)

# ==========================================
# Row 1: Developer → GitHub → Jenkins
# ==========================================

# Developer
draw_box(0.5, 6.5, 2, 1.2, PURPLE, 'Developer', '👨‍💻')

# Arrow: Developer → GitHub
draw_arrow(2.5, 7.1, 3.5, 7.1, GRAY)
ax.text(3, 7.4, 'git push', ha='center', va='bottom', fontsize=9, color=GRAY)

# GitHub
draw_box(3.5, 6.5, 2, 1.2, DARK, 'GitHub', '📦')

# Arrow: GitHub → Jenkins
draw_arrow(5.5, 7.1, 6.5, 7.1, GRAY)
ax.text(6, 7.4, 'webhook', ha='center', va='bottom', fontsize=9, color=GRAY)

# Jenkins
draw_box(6.5, 6.5, 2.2, 1.2, ORANGE, 'Jenkins', '🔧')

# ==========================================
# Row 2: Jenkins Pipeline Stages
# ==========================================

# Pipeline box (large container)
pipeline_box = FancyBboxPatch((0.5, 3.5), 10.5, 2.5, 
                               boxstyle="round,pad=0.05,rounding_size=0.2",
                               facecolor='#1e293b', edgecolor=BLUE, linewidth=2, linestyle='--')
ax.add_patch(pipeline_box)
ax.text(5.75, 5.8, '⚡ Jenkins Pipeline Stages', ha='center', va='center', 
        fontsize=12, color=BLUE, fontweight='bold')

# Pipeline stages
stages = [
    (0.8, 4.0, 'Build', '🔨', BLUE),
    (2.6, 4.0, 'Test', '🧪', CYAN),
    (4.4, 4.0, 'Docker', '🐳', BLUE),
    (6.2, 4.0, 'Security', '🔒', PINK),
    (8.0, 4.0, 'Deploy', '🚀', GREEN),
    (9.8, 4.0, 'Health', '💚', GREEN),
]

for i, (x, y, text, icon, color) in enumerate(stages):
    draw_box(x, y, 1.5, 1.2, color, text, icon, fontsize=9)
    if i < len(stages) - 1:
        draw_arrow(x + 1.5, y + 0.6, x + 1.8, y + 0.6, GRAY)

# Arrow from Jenkins to Pipeline
draw_arrow(7.6, 6.5, 5.75, 6.0, GRAY)

# ==========================================
# Row 3: Docker & Deployment
# ==========================================

# Docker Container
draw_box(12, 6.0, 2.5, 1.5, BLUE, 'Docker\nContainer', '🐳', fontsize=10)

# Arrow from Pipeline to Docker
draw_arrow(11, 4.6, 12, 6.5, GRAY)

# Server/Cloud
draw_box(12, 3.5, 2.5, 1.5, CYAN, 'Server\nDeployment', '☁️', fontsize=10)

# Arrow from Docker to Server
draw_arrow(13.25, 6.0, 13.25, 5.0, GRAY)

# ==========================================
# Self-Healing Section
# ==========================================

# Self-healing box
heal_box = FancyBboxPatch((12, 1.0), 3.5, 2.0, 
                           boxstyle="round,pad=0.05,rounding_size=0.2",
                           facecolor='#1e293b', edgecolor=GREEN, linewidth=2)
ax.add_patch(heal_box)

ax.text(13.75, 2.7, '🔄 Self-Healing', ha='center', va='center', 
        fontsize=11, color=GREEN, fontweight='bold')
ax.text(13.75, 2.1, 'Ansible monitors health', ha='center', va='center', 
        fontsize=9, color=GRAY)
ax.text(13.75, 1.6, 'Auto-restart on failure', ha='center', va='center', 
        fontsize=9, color=GRAY)

# Arrow from Server to Self-healing
draw_dashed_arrow(13.25, 3.5, 13.25, 3.0, GREEN)

# ==========================================
# Dashboard & Monitoring
# ==========================================

# Dashboard
draw_box(0.5, 1.5, 2.5, 1.5, PURPLE, 'Dashboard', '🖥️', fontsize=10)
ax.text(1.75, 1.2, 'Real-time UI', ha='center', va='center', fontsize=8, color=GRAY)

# Arrow from Jenkins to Dashboard
draw_dashed_arrow(6.5, 6.5, 2.5, 2.5, PURPLE)

# ==========================================
# Notifications
# ==========================================

# Telegram
draw_box(4, 1.0, 2.5, 1.5, GREEN, 'Telegram', '📱', fontsize=10)
ax.text(5.25, 0.7, 'Instant Alerts', ha='center', va='center', fontsize=8, color=GRAY)

# Arrow from Jenkins to Telegram
draw_dashed_arrow(7.5, 6.5, 5.25, 2.5, GREEN)

# ==========================================
# Database
# ==========================================

draw_box(7.5, 1.0, 2, 1.5, YELLOW, 'MySQL\nDB', '🗄️', fontsize=10)

# Arrow from Server to DB
draw_dashed_arrow(12, 4.0, 9.5, 2.0, YELLOW)

# ==========================================
# Legend
# ==========================================

legend_y = 0.3
ax.text(0.5, legend_y, '━━ Data Flow', ha='left', va='center', fontsize=9, color=WHITE)
ax.text(3.5, legend_y, '╌╌ Monitoring', ha='left', va='center', fontsize=9, color=GRAY)

# Technologies used
ax.text(8, 0.3, 'Technologies: Java 17 • Spring Boot • Maven • Docker • Jenkins • Ansible • Trivy • Telegram', 
        ha='center', va='center', fontsize=9, color=GRAY)

# ==========================================
# Save Image
# ==========================================

plt.tight_layout()
output_path = r'C:\Users\hp\OneDrive\Desktop\Neo-Deploy\NeoDeploy-Architecture.png'
plt.savefig(output_path, dpi=150, facecolor='#0f172a', edgecolor='none', bbox_inches='tight')
print(f"✅ Architecture diagram saved to: {output_path}")

# Also save a white background version for PPT
fig2, ax2 = plt.subplots(1, 1, figsize=(16, 10), facecolor='white')
ax2.set_facecolor('white')
ax2.set_xlim(0, 16)
ax2.set_ylim(0, 10)
ax2.axis('off')

# Redraw with dark text colors for white background
WHITE_VER = '#1e293b'
GRAY_VER = '#64748b'

# Title
ax2.text(8, 9.5, '🚀 NeoDeploy - System Architecture', 
        ha='center', va='center', fontsize=24, color=WHITE_VER, fontweight='bold')
ax2.text(8, 9.0, 'CI/CD Pipeline with Self-Healing & Real-Time Monitoring', 
        ha='center', va='center', fontsize=12, color=GRAY_VER)

def draw_box2(x, y, width, height, color, text, icon='', fontsize=11):
    box = FancyBboxPatch((x, y), width, height, 
                          boxstyle="round,pad=0.05,rounding_size=0.15",
                          facecolor=color, edgecolor='#334155', linewidth=2)
    ax2.add_patch(box)
    display_text = f"{icon}\n{text}" if icon else text
    ax2.text(x + width/2, y + height/2, display_text, 
            ha='center', va='center', fontsize=fontsize, 
            color='white', fontweight='bold', linespacing=1.5)

def draw_arrow2(x1, y1, x2, y2, color='#334155'):
    ax2.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', color=color, lw=2.5))

# Developer
draw_box2(0.5, 6.5, 2, 1.2, PURPLE, 'Developer', '👨‍💻')
draw_arrow2(2.5, 7.1, 3.5, 7.1)
ax2.text(3, 7.4, 'git push', ha='center', va='bottom', fontsize=9, color=GRAY_VER)

# GitHub
draw_box2(3.5, 6.5, 2, 1.2, '#334155', 'GitHub', '📦')
draw_arrow2(5.5, 7.1, 6.5, 7.1)
ax2.text(6, 7.4, 'webhook', ha='center', va='bottom', fontsize=9, color=GRAY_VER)

# Jenkins
draw_box2(6.5, 6.5, 2.2, 1.2, ORANGE, 'Jenkins', '🔧')

# Pipeline box
pipeline_box2 = FancyBboxPatch((0.5, 3.5), 10.5, 2.5, 
                               boxstyle="round,pad=0.05,rounding_size=0.2",
                               facecolor='#f1f5f9', edgecolor=BLUE, linewidth=2, linestyle='--')
ax2.add_patch(pipeline_box2)
ax2.text(5.75, 5.8, '⚡ Jenkins Pipeline Stages', ha='center', va='center', 
        fontsize=12, color=BLUE, fontweight='bold')

# Pipeline stages
for i, (x, y, text, icon, color) in enumerate(stages):
    draw_box2(x, y, 1.5, 1.2, color, text, icon, fontsize=9)
    if i < len(stages) - 1:
        draw_arrow2(x + 1.5, y + 0.6, x + 1.8, y + 0.6)

draw_arrow2(7.6, 6.5, 5.75, 6.0)

# Docker Container
draw_box2(12, 6.0, 2.5, 1.5, BLUE, 'Docker\nContainer', '🐳', fontsize=10)
draw_arrow2(11, 4.6, 12, 6.5)

# Server
draw_box2(12, 3.5, 2.5, 1.5, CYAN, 'Server\nDeployment', '☁️', fontsize=10)
draw_arrow2(13.25, 6.0, 13.25, 5.0)

# Self-healing
heal_box2 = FancyBboxPatch((12, 1.0), 3.5, 2.0, 
                           boxstyle="round,pad=0.05,rounding_size=0.2",
                           facecolor='#f0fdf4', edgecolor=GREEN, linewidth=2)
ax2.add_patch(heal_box2)
ax2.text(13.75, 2.7, '🔄 Self-Healing', ha='center', va='center', 
        fontsize=11, color=GREEN, fontweight='bold')
ax2.text(13.75, 2.1, 'Ansible monitors health', ha='center', va='center', 
        fontsize=9, color=GRAY_VER)
ax2.text(13.75, 1.6, 'Auto-restart on failure', ha='center', va='center', 
        fontsize=9, color=GRAY_VER)

# Dashboard
draw_box2(0.5, 1.5, 2.5, 1.5, PURPLE, 'Dashboard', '🖥️', fontsize=10)
ax2.text(1.75, 1.2, 'Real-time UI', ha='center', va='center', fontsize=8, color=GRAY_VER)

# Telegram
draw_box2(4, 1.0, 2.5, 1.5, GREEN, 'Telegram', '📱', fontsize=10)
ax2.text(5.25, 0.7, 'Instant Alerts', ha='center', va='center', fontsize=8, color=GRAY_VER)

# Database
draw_box2(7.5, 1.0, 2, 1.5, YELLOW, 'MySQL\nDB', '🗄️', fontsize=10)

# Footer
ax2.text(8, 0.3, 'Technologies: Java 17 • Spring Boot • Maven • Docker • Jenkins • Ansible • Trivy • Telegram', 
        ha='center', va='center', fontsize=9, color=GRAY_VER)

plt.tight_layout()
output_path2 = r'C:\Users\hp\OneDrive\Desktop\Neo-Deploy\NeoDeploy-Architecture-Light.png'
plt.savefig(output_path2, dpi=150, facecolor='white', edgecolor='none', bbox_inches='tight')
print(f"✅ Light version saved to: {output_path2}")

print("\n📊 Both architecture diagrams created!")
print("   Dark theme:  NeoDeploy-Architecture.png")
print("   Light theme: NeoDeploy-Architecture-Light.png")

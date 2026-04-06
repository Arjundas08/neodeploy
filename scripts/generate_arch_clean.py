"""
NeoDeploy - Clean System Architecture Diagram (No Emojis)
"""

import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch, Circle
import numpy as np

# Create figure
fig, ax = plt.subplots(1, 1, figsize=(16, 10), facecolor='white')
ax.set_facecolor('white')
ax.set_xlim(0, 16)
ax.set_ylim(0, 10)
ax.axis('off')

# Colors
BLUE = '#3b82f6'
GREEN = '#22c55e'
PURPLE = '#8b5cf6'
ORANGE = '#f97316'
CYAN = '#06b6d4'
PINK = '#ec4899'
YELLOW = '#eab308'
DARK = '#1e293b'
GRAY = '#64748b'
LIGHT_GRAY = '#f1f5f9'

def draw_box(x, y, width, height, color, text, subtitle='', fontsize=12):
    """Draw a rounded rectangle box with text"""
    box = FancyBboxPatch((x, y), width, height, 
                          boxstyle="round,pad=0.05,rounding_size=0.2",
                          facecolor=color, edgecolor='white', linewidth=2.5)
    ax.add_patch(box)
    
    if subtitle:
        ax.text(x + width/2, y + height/2 + 0.15, text, 
                ha='center', va='center', fontsize=fontsize, 
                color='white', fontweight='bold')
        ax.text(x + width/2, y + height/2 - 0.25, subtitle, 
                ha='center', va='center', fontsize=fontsize-3, 
                color='white', alpha=0.9)
    else:
        ax.text(x + width/2, y + height/2, text, 
                ha='center', va='center', fontsize=fontsize, 
                color='white', fontweight='bold')

def draw_arrow(x1, y1, x2, y2, color=GRAY, style='-'):
    """Draw an arrow between two points"""
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', color=color, lw=2.5, 
                               linestyle=style, shrinkA=5, shrinkB=5))

# ==========================================
# Title
# ==========================================
ax.text(8, 9.5, 'NeoDeploy - System Architecture', 
        ha='center', va='center', fontsize=28, color=DARK, fontweight='bold')
ax.text(8, 8.95, 'CI/CD Pipeline with Self-Healing & Real-Time Monitoring', 
        ha='center', va='center', fontsize=14, color=GRAY)

# ==========================================
# Row 1: Developer -> GitHub -> Jenkins
# ==========================================

# Developer
draw_box(0.3, 6.3, 2.2, 1.4, PURPLE, 'DEVELOPER')

# Arrow
draw_arrow(2.5, 7.0, 3.8, 7.0)
ax.text(3.15, 7.3, 'git push', ha='center', va='bottom', fontsize=10, color=GRAY, style='italic')

# GitHub
draw_box(3.8, 6.3, 2.2, 1.4, DARK, 'GITHUB')

# Arrow
draw_arrow(6.0, 7.0, 7.3, 7.0)
ax.text(6.65, 7.3, 'webhook', ha='center', va='bottom', fontsize=10, color=GRAY, style='italic')

# Jenkins
draw_box(7.3, 6.3, 2.4, 1.4, ORANGE, 'JENKINS', 'CI/CD Server')

# ==========================================
# Row 2: Jenkins Pipeline Stages
# ==========================================

# Pipeline container
pipeline_box = FancyBboxPatch((0.3, 3.3), 11.4, 2.5, 
                               boxstyle="round,pad=0.05,rounding_size=0.3",
                               facecolor=LIGHT_GRAY, edgecolor=BLUE, linewidth=3)
ax.add_patch(pipeline_box)
ax.text(6, 5.6, 'JENKINS PIPELINE STAGES', ha='center', va='center', 
        fontsize=14, color=BLUE, fontweight='bold')

# Pipeline stages
stages = [
    (0.6, 3.7, 'BUILD', 'Maven', BLUE),
    (2.5, 3.7, 'TEST', 'JUnit', CYAN),
    (4.4, 3.7, 'DOCKER', 'Build', BLUE),
    (6.3, 3.7, 'SECURITY', 'Trivy', PINK),
    (8.2, 3.7, 'DEPLOY', 'Run', GREEN),
    (10.1, 3.7, 'HEALTH', 'Check', GREEN),
]

for i, (x, y, text, sub, color) in enumerate(stages):
    draw_box(x, y, 1.7, 1.3, color, text, sub, fontsize=11)
    if i < len(stages) - 1:
        draw_arrow(x + 1.7, y + 0.65, x + 2.0, y + 0.65, GRAY)

# Arrow from Jenkins to Pipeline
draw_arrow(8.5, 6.3, 6, 5.8, GRAY)

# ==========================================
# Right side: Docker & Deployment
# ==========================================

# Docker Container
draw_box(12.5, 6.0, 2.8, 1.6, BLUE, 'DOCKER', 'Container')

# Arrow from Pipeline to Docker
draw_arrow(11.7, 4.5, 12.5, 6.5, GRAY)

# Server
draw_box(12.5, 3.3, 2.8, 1.6, CYAN, 'SERVER', 'Production')

# Arrow from Docker to Server
draw_arrow(13.9, 6.0, 13.9, 4.9, GRAY)

# ==========================================
# Self-Healing Section
# ==========================================

heal_box = FancyBboxPatch((12.2, 0.8), 3.4, 2.0, 
                           boxstyle="round,pad=0.05,rounding_size=0.2",
                           facecolor='#dcfce7', edgecolor=GREEN, linewidth=3)
ax.add_patch(heal_box)

ax.text(13.9, 2.5, 'SELF-HEALING', ha='center', va='center', 
        fontsize=13, color=GREEN, fontweight='bold')
ax.text(13.9, 1.9, 'Ansible monitors health', ha='center', va='center', 
        fontsize=10, color=DARK)
ax.text(13.9, 1.4, 'Auto-restart on failure', ha='center', va='center', 
        fontsize=10, color=DARK)

# Arrow from Server to Self-healing
draw_arrow(13.9, 3.3, 13.9, 2.8, GREEN, '--')

# ==========================================
# Left Bottom: Dashboard & Notifications
# ==========================================

# Dashboard
draw_box(0.3, 1.2, 2.5, 1.5, PURPLE, 'DASHBOARD', 'Real-time UI')

# Arrow from Jenkins to Dashboard
draw_arrow(7.3, 6.3, 2.0, 2.7, PURPLE, '--')

# Telegram
draw_box(3.5, 1.2, 2.5, 1.5, GREEN, 'TELEGRAM', 'Alerts')

# Arrow from Jenkins to Telegram
draw_arrow(8.5, 6.3, 4.75, 2.7, GREEN, '--')

# Database
draw_box(6.7, 1.2, 2.5, 1.5, YELLOW, 'DATABASE', 'MySQL/H2')

# Arrow from Server to DB
draw_arrow(12.5, 4.0, 9.2, 2.2, YELLOW, '--')

# ==========================================
# Legend
# ==========================================

# Legend box
legend_box = FancyBboxPatch((0.3, 0.1), 5, 0.6, 
                             boxstyle="round,pad=0.02,rounding_size=0.1",
                             facecolor='white', edgecolor=GRAY, linewidth=1)
ax.add_patch(legend_box)

ax.plot([0.5, 1.2], [0.4, 0.4], '-', color=DARK, lw=2)
ax.text(1.4, 0.4, 'Data Flow', ha='left', va='center', fontsize=9, color=DARK)

ax.plot([2.8, 3.5], [0.4, 0.4], '--', color=GRAY, lw=2)
ax.text(3.7, 0.4, 'Monitoring', ha='left', va='center', fontsize=9, color=DARK)

# Technologies footer
tech_text = 'Technologies: Java 17  |  Spring Boot  |  Maven  |  Docker  |  Jenkins  |  Ansible  |  Trivy  |  Telegram'
ax.text(8, -0.2, tech_text, ha='center', va='center', fontsize=10, color=GRAY, style='italic')

# ==========================================
# Save
# ==========================================

plt.tight_layout()
output_path = r'C:\Users\hp\OneDrive\Desktop\Neo-Deploy\NeoDeploy-Architecture.png'
plt.savefig(output_path, dpi=200, facecolor='white', edgecolor='none', 
            bbox_inches='tight', pad_inches=0.3)
print(f"✅ Architecture diagram saved: {output_path}")

# Also create dark version
fig2, ax2 = plt.subplots(1, 1, figsize=(16, 10), facecolor='#0f172a')
ax2.set_facecolor('#0f172a')
ax2.set_xlim(0, 16)
ax2.set_ylim(0, 10)
ax2.axis('off')

def draw_box_dark(x, y, width, height, color, text, subtitle='', fontsize=12):
    box = FancyBboxPatch((x, y), width, height, 
                          boxstyle="round,pad=0.05,rounding_size=0.2",
                          facecolor=color, edgecolor='#334155', linewidth=2.5)
    ax2.add_patch(box)
    
    if subtitle:
        ax2.text(x + width/2, y + height/2 + 0.15, text, 
                ha='center', va='center', fontsize=fontsize, 
                color='white', fontweight='bold')
        ax2.text(x + width/2, y + height/2 - 0.25, subtitle, 
                ha='center', va='center', fontsize=fontsize-3, 
                color='white', alpha=0.9)
    else:
        ax2.text(x + width/2, y + height/2, text, 
                ha='center', va='center', fontsize=fontsize, 
                color='white', fontweight='bold')

def draw_arrow_dark(x1, y1, x2, y2, color='#64748b', style='-'):
    ax2.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', color=color, lw=2.5, 
                               linestyle=style, shrinkA=5, shrinkB=5))

# Title
ax2.text(8, 9.5, 'NeoDeploy - System Architecture', 
        ha='center', va='center', fontsize=28, color='white', fontweight='bold')
ax2.text(8, 8.95, 'CI/CD Pipeline with Self-Healing & Real-Time Monitoring', 
        ha='center', va='center', fontsize=14, color='#94a3b8')

# Developer
draw_box_dark(0.3, 6.3, 2.2, 1.4, PURPLE, 'DEVELOPER')
draw_arrow_dark(2.5, 7.0, 3.8, 7.0)
ax2.text(3.15, 7.3, 'git push', ha='center', va='bottom', fontsize=10, color='#94a3b8', style='italic')

# GitHub
draw_box_dark(3.8, 6.3, 2.2, 1.4, '#334155', 'GITHUB')
draw_arrow_dark(6.0, 7.0, 7.3, 7.0)
ax2.text(6.65, 7.3, 'webhook', ha='center', va='bottom', fontsize=10, color='#94a3b8', style='italic')

# Jenkins
draw_box_dark(7.3, 6.3, 2.4, 1.4, ORANGE, 'JENKINS', 'CI/CD Server')

# Pipeline container
pipeline_box2 = FancyBboxPatch((0.3, 3.3), 11.4, 2.5, 
                               boxstyle="round,pad=0.05,rounding_size=0.3",
                               facecolor='#1e293b', edgecolor=BLUE, linewidth=3)
ax2.add_patch(pipeline_box2)
ax2.text(6, 5.6, 'JENKINS PIPELINE STAGES', ha='center', va='center', 
        fontsize=14, color=BLUE, fontweight='bold')

# Pipeline stages
for i, (x, y, text, sub, color) in enumerate(stages):
    draw_box_dark(x, y, 1.7, 1.3, color, text, sub, fontsize=11)
    if i < len(stages) - 1:
        draw_arrow_dark(x + 1.7, y + 0.65, x + 2.0, y + 0.65, '#64748b')

draw_arrow_dark(8.5, 6.3, 6, 5.8, '#64748b')

# Docker
draw_box_dark(12.5, 6.0, 2.8, 1.6, BLUE, 'DOCKER', 'Container')
draw_arrow_dark(11.7, 4.5, 12.5, 6.5, '#64748b')

# Server
draw_box_dark(12.5, 3.3, 2.8, 1.6, CYAN, 'SERVER', 'Production')
draw_arrow_dark(13.9, 6.0, 13.9, 4.9, '#64748b')

# Self-healing
heal_box2 = FancyBboxPatch((12.2, 0.8), 3.4, 2.0, 
                           boxstyle="round,pad=0.05,rounding_size=0.2",
                           facecolor='#1e293b', edgecolor=GREEN, linewidth=3)
ax2.add_patch(heal_box2)
ax2.text(13.9, 2.5, 'SELF-HEALING', ha='center', va='center', 
        fontsize=13, color=GREEN, fontweight='bold')
ax2.text(13.9, 1.9, 'Ansible monitors health', ha='center', va='center', 
        fontsize=10, color='#94a3b8')
ax2.text(13.9, 1.4, 'Auto-restart on failure', ha='center', va='center', 
        fontsize=10, color='#94a3b8')
draw_arrow_dark(13.9, 3.3, 13.9, 2.8, GREEN, '--')

# Dashboard
draw_box_dark(0.3, 1.2, 2.5, 1.5, PURPLE, 'DASHBOARD', 'Real-time UI')
draw_arrow_dark(7.3, 6.3, 2.0, 2.7, PURPLE, '--')

# Telegram
draw_box_dark(3.5, 1.2, 2.5, 1.5, GREEN, 'TELEGRAM', 'Alerts')
draw_arrow_dark(8.5, 6.3, 4.75, 2.7, GREEN, '--')

# Database
draw_box_dark(6.7, 1.2, 2.5, 1.5, YELLOW, 'DATABASE', 'MySQL/H2')
draw_arrow_dark(12.5, 4.0, 9.2, 2.2, YELLOW, '--')

# Footer
ax2.text(8, -0.2, tech_text, ha='center', va='center', fontsize=10, color='#64748b', style='italic')

plt.tight_layout()
output_path2 = r'C:\Users\hp\OneDrive\Desktop\Neo-Deploy\NeoDeploy-Architecture-Dark.png'
plt.savefig(output_path2, dpi=200, facecolor='#0f172a', edgecolor='none', 
            bbox_inches='tight', pad_inches=0.3)
print(f"✅ Dark version saved: {output_path2}")

print("\n📊 Architecture diagrams created successfully!")
print(f"   Light: NeoDeploy-Architecture.png")
print(f"   Dark:  NeoDeploy-Architecture-Dark.png")

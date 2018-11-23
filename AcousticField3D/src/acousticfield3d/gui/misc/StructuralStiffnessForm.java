/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acousticfield3d.gui.misc;

import acousticfield3d.algorithms.CalcField;
import acousticfield3d.gui.MainForm;
import acousticfield3d.math.Vector3f;
import acousticfield3d.scene.MeshEntity;
import acousticfield3d.scene.Scene;
import acousticfield3d.utils.Parse;
import acousticfield3d.utils.TextFrame;

/**
 *
 * @author am14010
 */
public class StructuralStiffnessForm extends javax.swing.JFrame {
    final MainForm mf;
    
    public StructuralStiffnessForm(MainForm mf) {
        this.mf = mf;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        nPointsText = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        dispText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        rotText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        scaleText = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("N Points:");

        nPointsText.setText("120");

        jButton1.setText("Calc");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Displacement:");

        dispText.setText("0 0 0");

        jLabel3.setText("Rotation (deg):");

        rotText.setText("0 0 0");

        jLabel4.setText("Scale:");

        scaleText.setText("1 1 1");
        scaleText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scaleTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(nPointsText, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(dispText))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rotText)
                            .addComponent(scaleText))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nPointsText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(dispText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(rotText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(scaleText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        final int nPoints = Parse.toInt( nPointsText.getText() );
        final Vector3f dis = new Vector3f().parse( dispText.getText() );
        final Vector3f rot = new Vector3f().parse( rotText.getText() );
        final Vector3f scale = new Vector3f().parse( scaleText.getText() );
        
        final StringBuilder sb = new StringBuilder();
        
        //snap the position of the points
        mf.movePanel.snapBeadPositions();
        for (int i = 0; i < nPoints; i++) {
            //apply the algorithm
            mf.movePanel.applyVector(0, 0, 0);
            
            //calc center of the structure (is it the center of mass??)
            final Vector3f center = Scene.calcCenter( mf.simulation.controlPoints );
            final Vector3f tStiffness = new Vector3f();
            final Vector3f tTorqueStiff = new Vector3f();
            final Vector3f r = new Vector3f();
            for (MeshEntity point : mf.simulation.controlPoints) {
                final Vector3f pos = point.getTransform().getTranslation();
                //calc total stiffness
                final Vector3f forceGrad = CalcField.calcForceGradients(pos.x, pos.y, pos.z, 
                        point.getTransform().getScale().maxComponent(), mf);
                tStiffness.addLocal( forceGrad );
                
                //calc the total torque stiffness
                pos.subtract( center, r);
                tTorqueStiff.addLocal( r.crossLocal(forceGrad) );
            }
            //report
            sb.append(i + "\t" + tStiffness.toStringSimple("\t") + tTorqueStiff.toStringSimple("\t") + "\n");
            
            //apply increment
            //TODO
        }
        
        //restore the position of the points
        mf.movePanel.resetParticlePos();
        
        TextFrame.showText("Stiffness forces and torque", sb.toString(), this);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void scaleTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scaleTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scaleTextActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField dispText;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField nPointsText;
    private javax.swing.JTextField rotText;
    private javax.swing.JTextField scaleText;
    // End of variables declaration//GEN-END:variables
}

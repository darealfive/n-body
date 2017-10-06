package com.company;

class BarnesHutTree {

    private SpaceTime space;

    BarnesHutTree(SpaceTime space) {

        this.space = space;
    }

    TreeNode build(double delta) {

        TreeNode rootTreeNode = new TreeNode(this.space.getWidth(), 0, 0);
        for (NodeInterface body : space.getBodies()) {

            rootTreeNode.add(body);
        }

        for (Attractable body : space.getBodies()) {

            rootTreeNode.calculateForce(body, delta);
        }

        return rootTreeNode;
    }
}

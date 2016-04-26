#! /usr/bin/env sh
UPDATE_SITE_DIR=releng/eu.modelwriter.updatesite/target/repository
UPDATE_SITE_GPL_DIR=releng/eu.modelwriter.gpl.updatesite/target/repository
DEPLOY_LOCAL_DIR=$1/repository
DEPLOY_GPL_LOCAL_DIR=$1/repository_gpl
echo "Prepare deploy local dir = ${DEPLOY_LOCAL_DIR}"
# Create deploy local dir
mkdir -p $DEPLOY_LOCAL_DIR 
mkdir -p $DEPLOY_GPL_LOCAL_DIR 
# Copy update-site to deploy local dir
cp -r $UPDATE_SITE_DIR/* $DEPLOY_LOCAL_DIR
cp -r $UPDATE_SITE_GPL_DIR/* $DEPLOY_GPL_LOCAL_DIR
echo "ls ${DEPLOY_LOCAL_DIR}"
ls $DEPLOY_LOCAL_DIR
echo "ls ${DEPLOY_GPL_LOCAL_DIR}"
ls $DEPLOY_GPL_LOCAL_DIR


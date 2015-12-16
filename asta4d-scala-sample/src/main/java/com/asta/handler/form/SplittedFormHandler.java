/*
 * Copyright 2014 astamuse company,Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.asta.handler.form;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.asta.handler.form.SplittedForm.CascadeJobForm;
import com.asta.handler.form.common.Asta4DSamplePrjCommonFormHandler;
import com.asta.util.persondb.JobExperence;
import com.asta.util.persondb.JobExperenceDbManager;
import com.asta.util.persondb.JobPosition;
import com.asta.util.persondb.JobPositionDbManager;
import com.asta.util.persondb.PersonDbManager;
import com.astamuse.asta4d.util.collection.ListConvertUtil;
import com.astamuse.asta4d.util.collection.RowConvertor;
import com.astamuse.asta4d.web.form.flow.ext.MultiInputStepFormFlowHandlerTrait;
import com.astamuse.asta4d.web.util.message.DefaultMessageRenderingHelper;

//@ShowCode:showSplittedFormHandlerStart
public abstract class SplittedFormHandler extends Asta4DSamplePrjCommonFormHandler<SplittedForm> implements
        MultiInputStepFormFlowHandlerTrait<SplittedForm> {

    public Class<SplittedForm> getFormCls() {
        return SplittedForm.class;
    }

    @Override
    public String[] getInputSteps() {
        return new String[] { SplittedForm.inputStep1, SplittedForm.inputStep2 };
    }

    @Override
    public SplittedForm generateFormInstanceFromContext(String currentStep) {
        SplittedForm form = super.generateFormInstanceFromContext(currentStep);

        if (SplittedForm.inputStep2.equalsIgnoreCase(currentStep)) {
            // rewrite the array to handle deleted items
            CascadeJobForm cjForm = form.getCascadeJobForm();

            List<JobForm> rewriteJobList = new LinkedList<>();
            for (JobForm jobform : cjForm.getJobForms()) {
                List<JobPositionForm> rewritePosList = new LinkedList<>();
                // remove rows without job id specified
                for (JobPositionForm posForm : jobform.getJobPositionForms()) {
                    if (posForm.getJobId() != null) {
                        rewritePosList.add(posForm);
                    }
                }
                jobform.setJobPositionForms(rewritePosList.toArray(new JobPositionForm[rewritePosList.size()]));
                jobform.setJobPositionLength(jobform.getJobPositionForms().length);

                // remove rows without person id specified
                if (jobform.getPersonId() != null) {
                    rewriteJobList.add(jobform);
                }
            }
            cjForm.setJobForms(rewriteJobList.toArray(new JobForm[rewriteJobList.size()]));
            cjForm.setJobExperienceLength(cjForm.getJobForms().length);

        }

        return form;
    }

    /**
     * we do add logic by Add handler
     * 
     */
    public static class Add extends SplittedFormHandler {

        @Override
        public SplittedForm createInitForm() {
            SplittedForm form = new SplittedForm();

            return form;
        }

        @Override
        public void updateForm(SplittedForm form) {
            PersonForm pForm = form.getPersonForm();
            PersonDbManager.instance().add(pForm);
            for (JobForm jobForm : form.getCascadeJobForm().getJobForms()) {
                jobForm.setPersonId(pForm.getId());
                JobExperenceDbManager.instance().add(jobForm);
                for (JobPositionForm posForm : jobForm.getJobPositionForms()) {
                    posForm.setJobId(jobForm.getId());
                    JobPositionDbManager.instance().add(posForm);
                }
            }

            DefaultMessageRenderingHelper.getConfiguredInstance().info("data inserted");
        }
    }

    /**
     * we do update logic by Edit handler
     * 
     */
    public static class Edit extends SplittedFormHandler {

        @Override
        public SplittedForm createInitForm() throws Exception {
            SplittedForm superform = super.createInitForm();

            PersonForm pForm = PersonForm.buildFromPerson(PersonDbManager.instance().find(superform.getPersonForm().getId()));

            List<JobExperence> jobs = JobExperenceDbManager.instance().find("personId", pForm.getId());
            List<JobForm> jobFormList = ListConvertUtil.transform(jobs, new RowConvertor<JobExperence, JobForm>() {
                @Override
                public JobForm convert(int rowIndex, JobExperence j) {
                    return JobForm.buildFromJob(j);
                }
            });
            JobForm[] jobForms = jobFormList.toArray(new JobForm[jobFormList.size()]);

            for (JobForm jobform : jobForms) {
                List<JobPosition> posList = JobPositionDbManager.instance().find("jobId", jobform.getId());
                List<JobPositionForm> posFormList = ListConvertUtil.transform(posList, new RowConvertor<JobPosition, JobPositionForm>() {
                    @Override
                    public JobPositionForm convert(int rowIndex, JobPosition jp) {
                        return JobPositionForm.buildFromJobPosition(jp);
                    }
                });
                JobPositionForm[] posForms = posFormList.toArray(new JobPositionForm[posFormList.size()]);
                jobform.setJobPositionForms(posForms);
                jobform.setJobPositionLength(posForms.length);
            }

            CascadeJobForm cjForm = new CascadeJobForm();
            cjForm.setJobForms(jobForms);
            cjForm.setJobExperienceLength(jobForms.length);

            SplittedForm form = new SplittedForm();
            form.setPersonForm(pForm);
            form.setCascadeJobForm(cjForm);

            return form;
        }

        private final boolean isExistingId(Integer id) {
            if (id == null) {
                return false;
            } else {
                return id > 0;
            }
        }

        @Override
        public void updateForm(SplittedForm form) {
            PersonForm pForm = form.getPersonForm();
            PersonDbManager.instance().update(pForm);

            Set<Integer> validJobIds = new HashSet<>();
            Set<Integer> validPosIds = new HashSet<>();
            for (JobForm jobForm : form.getCascadeJobForm().getJobForms()) {
                jobForm.setPersonId(pForm.getId());
                if (isExistingId(jobForm.getId())) {
                    JobExperenceDbManager.instance().update(jobForm);
                } else {
                    JobExperenceDbManager.instance().add(jobForm);
                }
                validJobIds.add(jobForm.getId());

                for (JobPositionForm posForm : jobForm.getJobPositionForms()) {
                    posForm.setJobId(jobForm.getId());
                    if (isExistingId(posForm.getId())) {
                        JobPositionDbManager.instance().update(posForm);
                    } else {
                        JobPositionDbManager.instance().add(posForm);
                    }
                    validPosIds.add(posForm.getId());
                }
            }

            List<JobExperence> jobList = JobExperenceDbManager.instance().find("personId", pForm.getId());
            for (JobExperence job : jobList) {
                List<JobPosition> posList = JobPositionDbManager.instance().find("jobId", job.getId());
                for (JobPosition pos : posList) {
                    if (!validPosIds.contains(pos.getId())) {
                        JobPositionDbManager.instance().remove(pos);
                    }
                }
                if (!validJobIds.contains(job.getId())) {
                    JobExperenceDbManager.instance().remove(job);
                }
            }

            DefaultMessageRenderingHelper.getConfiguredInstance().info("update succeed");
        }

    }

}
// @ShowCode:showSplittedFormHandlerEnd

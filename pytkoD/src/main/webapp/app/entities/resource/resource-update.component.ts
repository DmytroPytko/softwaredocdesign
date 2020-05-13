import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IResource, Resource } from 'app/shared/model/resource.model';
import { ResourceService } from './resource.service';
import { IEmployee } from 'app/shared/model/employee.model';
import { EmployeeService } from 'app/entities/employee/employee.service';
import { IContractType } from 'app/shared/model/contract-type.model';
import { ContractTypeService } from 'app/entities/contract-type/contract-type.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project/project.service';

type SelectableEntity = IEmployee | IContractType | IProject;

@Component({
  selector: 'jhi-resource-update',
  templateUrl: './resource-update.component.html'
})
export class ResourceUpdateComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];
  contracttypes: IContractType[] = [];
  projects: IProject[] = [];

  editForm = this.fb.group({
    id: [],
    startDate: [],
    endDate: [],
    note: [],
    employee: [],
    contractType: [],
    project: []
  });

  constructor(
    protected resourceService: ResourceService,
    protected employeeService: EmployeeService,
    protected contractTypeService: ContractTypeService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resource }) => {
      if (!resource.id) {
        const today = moment().startOf('day');
        resource.startDate = today;
        resource.endDate = today;
      }

      this.updateForm(resource);

      this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

      this.contractTypeService.query().subscribe((res: HttpResponse<IContractType[]>) => (this.contracttypes = res.body || []));

      this.projectService.query().subscribe((res: HttpResponse<IProject[]>) => (this.projects = res.body || []));
    });
  }

  updateForm(resource: IResource): void {
    this.editForm.patchValue({
      id: resource.id,
      startDate: resource.startDate ? resource.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: resource.endDate ? resource.endDate.format(DATE_TIME_FORMAT) : null,
      note: resource.note,
      employee: resource.employee,
      contractType: resource.contractType,
      project: resource.project
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resource = this.createFromForm();
    if (resource.id !== undefined) {
      this.subscribeToSaveResponse(this.resourceService.update(resource));
    } else {
      this.subscribeToSaveResponse(this.resourceService.create(resource));
    }
  }

  private createFromForm(): IResource {
    return {
      ...new Resource(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? moment(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? moment(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      note: this.editForm.get(['note'])!.value,
      employee: this.editForm.get(['employee'])!.value,
      contractType: this.editForm.get(['contractType'])!.value,
      project: this.editForm.get(['project'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResource>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}

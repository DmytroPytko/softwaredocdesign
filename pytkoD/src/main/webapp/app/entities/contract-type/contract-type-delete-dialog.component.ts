import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContractType } from 'app/shared/model/contract-type.model';
import { ContractTypeService } from './contract-type.service';

@Component({
  templateUrl: './contract-type-delete-dialog.component.html'
})
export class ContractTypeDeleteDialogComponent {
  contractType?: IContractType;

  constructor(
    protected contractTypeService: ContractTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('contractTypeListModification');
      this.activeModal.close();
    });
  }
}

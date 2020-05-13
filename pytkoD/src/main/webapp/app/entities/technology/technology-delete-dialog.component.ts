import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITechnology } from 'app/shared/model/technology.model';
import { TechnologyService } from './technology.service';

@Component({
  templateUrl: './technology-delete-dialog.component.html'
})
export class TechnologyDeleteDialogComponent {
  technology?: ITechnology;

  constructor(
    protected technologyService: TechnologyService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.technologyService.delete(id).subscribe(() => {
      this.eventManager.broadcast('technologyListModification');
      this.activeModal.close();
    });
  }
}

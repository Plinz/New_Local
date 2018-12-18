import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { HoldingService } from '../entities/holding/holding.service';
import { IHolding } from '../shared/model/holding.model';
import { JhiDataUtils } from 'ng-jhipster';

@Component({
    selector: 'jhi-holding-delete-dialog',
    templateUrl: './holdingProfil-delete-dialog.component.html'
})
export class HoldingProfilDeleteDialogComponent {
    holding: IHolding;

    constructor(
        private holdingService: HoldingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.holdingService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'holdingListModification',
                content: 'Deleted an holding'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-holding-delete-popup',
    template: ''
})
export class HoldingProfilDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ holding }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(HoldingProfilDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.holding = holding;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
